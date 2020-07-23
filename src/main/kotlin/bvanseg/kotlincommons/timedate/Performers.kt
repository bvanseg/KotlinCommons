package bvanseg.kotlincommons.timedate

import bvanseg.kotlincommons.KotlinCommons
import bvanseg.kotlincommons.timedate.transformer.BoundedContext
import bvanseg.kotlincommons.timedate.transformer.into
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit
import kotlin.time.seconds

inline fun sleep(container: TimeContext) = TimeUnit.MILLISECONDS.sleep(container.asMillis)
suspend fun CoroutineScope.delay(container: TimeContext) = delay(container.asMillis)

/**
 * A time scheduling context, which is used to scheduling either blocking or non-blocking performances within time
 * constraints. This requires a *time pattern*, which is constructed via the DSL (see [perform]).
 * This at least requires a [BoundedContext] which is a context with an upper and lower time bounds such as
 * `now until 10.minutes from now` which is used for knowing what times are being used for execution. This could be
 * any kind of bounds such as `(1.hours from now) until (2.hours from now)`, which will wait until the time is within
 * those two bounds, and use it as a minimal start condition and maximal start condition. The [frequency] is used
 * for constructing how often we are going to perform. This is created *after* the `every` operator, and could be
 * `1.minutes.exactly` which would mean we are performing every 1 minute exactly. [exactly] means we are going to
 * wait until we are on the minute every minute before performing our action. If the time is `09:31:13` it will calculate
 * how much time before now and the next minute to execute, such that we perform our action at `09:32:00` with respect
 * to millisecond precision.
 */
class TimeScheduleContext(val boundedContext: BoundedContext, val frequency: TimePerformer): TimeContext by boundedContext {

    private val unit: UnitBasedTimeContainer = when(frequency.inner){
        is UnitBasedTimeContainer -> frequency.inner
        is TimePerformer ->
            when(frequency.inner){
                is UnitBasedTimeContainer -> frequency.inner
                is LocalDateTimeContainer -> frequency.inner.toUnitBasedTimeContainer()
                else -> TODO("That won't work!")
            }
        else -> TODO("That won't work!")
    }

    val freq = boundedContext.asSeconds / unit.asSeconds

    /**
     * Perform a blocking action such that it is within a time context. This requires a [TimePerformer] such that
     * the context of how to time the performance, whether it be an initial delay, exact time execution, and/or some
     * kind of iteration count based on how long to run, how often to run, how to run, when to run.
     *
     * ```
     * val start = now
     * (start until (10.minutes from start)         //from start/[now] until 10.minutes from start/[now]
     *      every (1.minutes.exactly                //Every 1 minute exactly
     *          (waitUntil 1.minutes)               //wait until 1 minute of the hour
     *          (starting (1.minutes from start)))  //starting 1 minute from start/now
     *      .perform{                               //Perform this...
     *          println("Hello, world!")
     *      }
     * ```
     *
     * @author Boston Vanseghi
     * @author Alex Couch
     */
    infix fun perform(callback: () -> Unit) {
        var tracker: Long

        handleStart()

        do {
            tracker = 0L
            handleWait()
            while (true) {
                callback()
                val start = now
                if (frequency.flag == TimePerformerFlag.EXACTLY) {
                    val frequencyContainer = frequency.inner as UnitBasedTimeContainer
                    val delta = frequencyContainer from start
                    val end = delta.asMillis - start.asMillis
                    KotlinCommons.KC_LOGGER.debug("CYCLE_EXACTLY - Sleeping for $end milliseconds. Starting Time: $start, Expected Awake Time: ${start + end.toInt().millis}")
                    TimeUnit.MILLISECONDS.sleep(end)
                    KotlinCommons.KC_LOGGER.debug("CYCLE_EXACTLY - Awakened from sleep at $now")
                } else {
                    val sleep = frequency.inner.asMillis
                    KotlinCommons.KC_LOGGER.debug("CYCLE_PRONTO - Sleeping for $sleep milliseconds. Starting Time: $sleep, Expected Awake Time: ${start + sleep.toInt().millis}")
                    sleep(frequency)
                    KotlinCommons.KC_LOGGER.debug("CYCLE_PRONTO - Awakened from sleep at $now")
                }
                tracker++
                if (tracker >= freq) {
                    KotlinCommons.KC_LOGGER.debug("PERFORM_FINISH - Finished performance at $now, $tracker/$freq cycles completed.")
                    break
                }
            }
        } while(frequency.waitUntilCondition.isSome)
    }

    fun performAsync(callback: suspend () -> Unit): Job {
        var tracker: Long

        handleStart()

        return GlobalScope.launch {
            do {
                tracker = 0L
                handleWait()
                while (true) {
                    callback()
                    val start = now
                    if (frequency.flag == TimePerformerFlag.EXACTLY) {
                        val frequencyContainer = frequency.inner as UnitBasedTimeContainer
                        val delta = frequencyContainer from start
                        val end = delta.asMillis - start.asMillis
                        KotlinCommons.KC_LOGGER.debug("CYCLE_EXACTLY - Sleeping for $end milliseconds. Starting Time: $start, Expected Awake Time: ${start + end.toInt().millis}")
                        delay(end)
                        KotlinCommons.KC_LOGGER.debug("CYCLE_EXACTLY - Awakened from sleep at $now")
                    } else {
                        val sleep = frequency.inner.asMillis
                        KotlinCommons.KC_LOGGER.debug("CYCLE_PRONTO - Sleeping for $sleep milliseconds. Starting Time: $sleep, Expected Awake Time: ${start + sleep.toInt().millis}")
                        sleep(frequency)
                        KotlinCommons.KC_LOGGER.debug("CYCLE_PRONTO - Awakened from sleep at $now")
                    }
                    tracker++
                    if (tracker >= freq) {
                        KotlinCommons.KC_LOGGER.debug("PERFORM_FINISH - Finished performance at $now, $tracker/$freq cycles completed.")
                        break
                    }
                }
            } while(frequency.waitUntilCondition.isSome)
        }
    }

    /**
     * Handles the starting condition of performances.
     *
     * @author Boston Vanseghi
     * @author Jacob Glickman
     */
    private fun handleStart() {

        /*
            If we have a lower bound greater than [now] then let's wait until [now] is within the bounded context
         */
        val lowerBounds = boundedContext.left
        val now = now
        if((lowerBounds into seconds) isAfter (now into seconds)){
            KotlinCommons.KC_LOGGER.debug("PRESTART_DELAY - Lower bounds of bounded time condition is $lowerBounds")
            val delta = ((lowerBounds into millis) - (now into millis))
            KotlinCommons.KC_LOGGER.debug("PRESTART_DELAY - Delaying start until time is within lower and upper bounds with $delta seconds of delay ")
            sleep(delta)
        }

        KotlinCommons.KC_LOGGER.debug("PERFORM_START - Starting performance at $now for $freq cycles.")

        /*
            If we have a starting condition, let's apply it as delay here.
            Starting conditions are, by default, at a "fixed" delay. "exactly" does not belong in this context.
         */
        if(frequency.startingCondition.isSome) {
            // We get our starting condition after checking it exists.
            val startCondition = frequency.startingCondition.unwrap.start
            val start = now
            // We want to get the starting condition as millisecond. If it's a delay of 1 minute, for example, this
            // yields 60,000ms.
            val sleep = startCondition.asMillis
            KotlinCommons.KC_LOGGER.debug("INIT_DELAY - Sleeping for $sleep milliseconds. Starting Time: $start, Expected Awake Time: ${start + sleep.toInt().millis}")
            TimeUnit.MILLISECONDS.sleep(sleep)
            KotlinCommons.KC_LOGGER.debug("INIT_DELAY - Awakened from sleep at $now")
        }
    }

    /**
     * Handles the waiting condition of performances. This function is exactly ONLY. a "pronto" context does not mean
     * anything here.
     *
     * @author Boston Vanseghi
     * @author Jacob Glickman
     */
    private fun handleWait() {
        if(frequency.waitUntilCondition.isSome) {
            // We extract the condition after verifying it exists.
            val waitUntilCondition = frequency.waitUntilCondition.unwrap
            // We're concerned with the unit that we have to wait, as we'll use it in further calculations to determine
            // how long exactly to sleep.
            val unit = (waitUntilCondition.start.inner as UnitBasedTimeContainer).unit
            val unitAsMillis = unit.valueInMillis

            val start = now
            // Here we're determining the maximum amount the unit can be as millis. We do this because we know that
            // we have to wait the amount of time in millis from the wait condition IN ADDITION TO our the remaining
            // amount of time in the current super-unit.
            // For example, say we wanted to execute on the 15th second of every minute, and our current minute is at
            // the 20th second. We know we have to travel that 40 seconds, and THEN the 15 seconds from our waitCondition
            // to reach our destination.
            val unitMaxInMillis = (unitAsMillis * unit.maxValueNoOverflow)
            val unitProgression = start.asMillis % unitMaxInMillis // Per our example above, the 40 seconds out of 60.

            val waitTimePerformer = waitUntilCondition.start
            val waitConditionMillis = waitTimePerformer.asMillis

            // Ex. if we want to wait until the 15 second mark, and we are at the 5 second mark, wait 10 seconds.
            if (unitProgression < waitConditionMillis) {
                val sleep = waitConditionMillis - unitProgression
                KotlinCommons.KC_LOGGER.debug("WAIT_UNTIL - Sleeping for $sleep milliseconds. Starting time: $start, Expected Awake Time: ${start + sleep.toInt().millis}")
                TimeUnit.MILLISECONDS.sleep(sleep)
                KotlinCommons.KC_LOGGER.debug("WAIT_UNTIL - Awakened from sleep at $now")
            }
            // Otherwise, if we are at the 20 second mark, and want to wait until the 15 second mark, we have to
            // wait 55 seconds: sleep = 60,000ms - 20,000ms + 15,000ms = 55,000ms
            else {
                val sleep = unitMaxInMillis - unitProgression + waitConditionMillis
                KotlinCommons.KC_LOGGER.debug("WAIT_UNTIL - Sleeping for $sleep milliseconds. Starting time: $start, Expected Awake Time: ${start + sleep.toInt().millis}")
                TimeUnit.MILLISECONDS.sleep(sleep)
                KotlinCommons.KC_LOGGER.debug("WAIT_UNTIL - Awakened from sleep at $now")
            }
        }
    }
}

infix fun BoundedContext.every(container: TimePerformer) = TimeScheduleContext(this, container)
infix fun BoundedContext.every(container: UnitBasedTimeContainer) = TimeScheduleContext(this, container.pronto)