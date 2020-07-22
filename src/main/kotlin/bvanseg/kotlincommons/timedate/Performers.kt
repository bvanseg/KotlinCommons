package bvanseg.kotlincommons.timedate

import bvanseg.kotlincommons.timedate.transformer.BoundedContext
import bvanseg.kotlincommons.timedate.transformer.into
import bvanseg.kotlincommons.timedate.transformer.truncate
import bvanseg.kotlincommons.timedate.transformer.until
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
        val freq = boundedContext.asSeconds / unit.unit.value

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
                println(delta)
                println("offset: $offset")
                val end = (delta truncate seconds) into seconds
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

    fun performAsync(callback: suspend () -> Unit) {
        var tracker = 0L
        val freq = boundedContext.asSeconds / unit.unit.value

        if(frequency.flag == TimePerformerFlag.EXACTLY){
            val start = now
            val end = (start into seconds) + unit into seconds
            val diff = start until end
            sleep(diff)
        }

        GlobalScope.launch {
            while(true) {
                callback()
                val waitFlag = frequency.waitUntilCondition
                if(waitFlag.isSome) {
                    val startCondition = frequency.waitUntilCondition.unwrap.start
                    val start = now
                    val end = (start into seconds) + (startCondition into seconds)
                    val diff = start until end
                    delay(diff)
                }
                else
                    delay(frequency)
                tracker++
                if(tracker >= freq)
                    break
            }
        }
    }
}

infix fun BoundedContext.every(container: TimePerformer) = TimeScheduleContext(this, container)