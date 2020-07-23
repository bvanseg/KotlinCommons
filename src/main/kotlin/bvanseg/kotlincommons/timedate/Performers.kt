package bvanseg.kotlincommons.timedate

import bvanseg.kotlincommons.KotlinCommons
import bvanseg.kotlincommons.timedate.transformer.BoundedContext
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

inline fun sleep(container: TimeContext) = TimeUnit.MILLISECONDS.sleep(container.asMillis)
suspend fun CoroutineScope.delay(container: TimeContext) = delay(container.asMillis)

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
     * Things to implement before finishing this method:
     *  We first must fully implement coercion. Coercion is used to calculate the frequency.
     */
    fun perform(callback: () -> Unit) {
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
            } while(frequency.startingCondition.isSome)
        }
    }

    /**
     * Handles the starting condition of performances.
     *
     * @author Boston Vanseghi
     * @author Jacob Glickman
     */
    private fun handleStart() {
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

            if (unitProgression < waitConditionMillis) {
                val sleep = waitConditionMillis - unitProgression
                KotlinCommons.KC_LOGGER.debug("WAIT_UNTIL - Sleeping for $sleep milliseconds. Starting time: $start, Expected Awake Time: ${start + sleep.toInt().millis}")
                TimeUnit.MILLISECONDS.sleep(sleep)
                KotlinCommons.KC_LOGGER.debug("WAIT_UNTIL - Awakened from sleep at $now")
            } else {
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