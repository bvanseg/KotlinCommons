package bvanseg.kotlincommons.timedate

import bvanseg.kotlincommons.KotlinCommons
import bvanseg.kotlincommons.timedate.transformer.BoundedContext
import bvanseg.kotlincommons.timedate.transformer.into
import bvanseg.kotlincommons.timedate.transformer.truncate
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

    /**
     * Things to implement before finishing this method:
     *  We first must fully implement coercion. Coercion is used to calculate the frequency.
     */
    fun perform(callback: () -> Unit) {
        var tracker = 0L
        val freq = boundedContext.asSeconds / unit.asSeconds

        KotlinCommons.KC_LOGGER.debug("PERFORM_START - Starting performance at $now for $freq cycles.")

        /*
            09:30:13 - starting condition
            09:31:13 - wait until condition
            09:32:13 - truncate
            09:32:00 - start performance
            09:33:00 - next performance
         */

        if(frequency.startingCondition.isSome) {
            // TODO: Differentiate between exactly and pronto here.
            val startCondition = frequency.startingCondition.unwrap.start
            val start = now
            val end = startCondition.asMillis - start.asMillis
            KotlinCommons.KC_LOGGER.debug("INIT_DELAY - Sleeping for $end milliseconds. Starting Time: $start, Expected Awake Time: ${start + end.toInt().millis}")
            TimeUnit.MILLISECONDS.sleep(end)
            KotlinCommons.KC_LOGGER.debug("INIT_DELAY - Awakened from sleep at $now")
        }

        // wait until an exactly or pronto
        if(frequency.waitUntilCondition.isSome) {
            // TODO: Differentiate between exactly and pronto here.
            val unit = (frequency.waitUntilCondition.unwrap.start.inner as UnitBasedTimeContainer).unit
            val unitAsMillis = unit.valueInMillis

            val start = now
            val unitMaxInMillis = (unitAsMillis * unit.maxValueNoOverflow)
            val unitProgression = start.asMillis % unitMaxInMillis

            val waitCondition = frequency.waitUntilCondition.unwrap.start
            val waitConditionMillis = waitCondition.asMillis

            println("unitMaxInMillis: $unitMaxInMillis")
            println("unitProgression: $unitProgression")
            println("unitAsMillis: $unitAsMillis")
            println("waitConditionMillis $waitConditionMillis")

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

        while(true) {
            callback()
            val start = now
            if(frequency.flag == TimePerformerFlag.EXACTLY) {
                val frequencyContainer = frequency.inner as UnitBasedTimeContainer
                val delta = frequencyContainer from start
                val end = delta.asMillis - start.asMillis
                KotlinCommons.KC_LOGGER.debug("CYCLE_EXACTLY - Sleeping for $end milliseconds. Starting Time: $start, Expected Awake Time: ${start + end.toInt().millis}")
                TimeUnit.MILLISECONDS.sleep(end)
                KotlinCommons.KC_LOGGER.debug("CYCLE_EXACTLY - Awakened from sleep at $now")
            }else {
                val sleep = frequency.inner.asMillis
                KotlinCommons.KC_LOGGER.debug("CYCLE_PRONTO - Sleeping for $sleep milliseconds. Starting Time: $sleep, Expected Awake Time: ${start + sleep.toInt().millis}")
                sleep(frequency)
                KotlinCommons.KC_LOGGER.debug("CYCLE_PRONTO - Awakened from sleep at $now")
            }
            tracker++
            if(tracker >= freq) {
                KotlinCommons.KC_LOGGER.debug("PERFORM_FINISH - Finished performance at $now, $tracker/$freq cycles completed.")
                break
            }
        }
    }

    fun performAsync(callback: suspend () -> Unit): Job {
        var tracker = 0L
        val freq = boundedContext.asSeconds / unit.asSeconds

        if(frequency.waitUntilCondition.isSome) {
            val waitCondition = frequency.waitUntilCondition.unwrap.start
            val start = now
            val delta = waitCondition from start
            val end = (delta truncate seconds) into seconds
            TimeUnit.MILLISECONDS.sleep(end.asMillis - start.asMillis)
        }

        return GlobalScope.launch {
            while(true) {
                val offset = System.currentTimeMillis() % 1000
                callback()
                if(frequency.waitUntilCondition.isSome) {
                    val waitCondition = frequency.waitUntilCondition.unwrap.start
                    val start = now
                    val delta = waitCondition from start
                    val end = (delta truncate seconds) into seconds
                    delay((end.asMillis - start.asMillis) - offset)
                }else {
                    delay(frequency)
                }
                tracker++
                println("tracker / frequency: $tracker / $freq")
                if(tracker >= freq)
                    break
            }
        }

    }
}

infix fun BoundedContext.every(container: TimePerformer) = TimeScheduleContext(this, container)
infix fun BoundedContext.every(container: UnitBasedTimeContainer) = TimeScheduleContext(this, container.pronto)