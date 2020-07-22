package bvanseg.kotlincommons.timedate

import bvanseg.kotlincommons.timedate.transformer.BoundedContext
import bvanseg.kotlincommons.timedate.transformer.into
import bvanseg.kotlincommons.timedate.transformer.truncate
import bvanseg.kotlincommons.timedate.transformer.until
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

        /*
            09:30:13 - starting condition
            09:31:13 - wait until condition
            09:32:13 - truncate
            09:32:00 - start performance
            09:33:00 - next performance
         */
        if(frequency.waitUntilCondition.isSome) {
            val waitCondition = frequency.waitUntilCondition.unwrap.start
            val start = now
            val delta = waitCondition from start
            val end = (delta truncate seconds) into seconds
            println("delta: $delta")
            println("end: $end")
            TimeUnit.MILLISECONDS.sleep(end.asMillis - start.asMillis)
            println("done sleeping")
        }

        while(true) {
            val offset = System.currentTimeMillis() % 1000
            callback()
            if(frequency.waitUntilCondition.isSome) {
                val waitCondition = frequency.waitUntilCondition.unwrap.start
                val start = now
                val delta = waitCondition from start
                println("delta: $delta")
                println("offset: $offset")
                val end = (delta truncate seconds) into seconds
                println("(end.asMillis - start.asMillis) - offset: ${(end.asMillis - start.asMillis) - offset}")
                TimeUnit.MILLISECONDS.sleep((end.asMillis - start.asMillis) - offset)
            }else {
                println("frequency millis: ${frequency.asMillis}")
                sleep(frequency)
            }
            tracker++
            if(tracker >= freq)
                break
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