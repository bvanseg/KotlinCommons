package bvanseg.kotlincommons.timedate

import bvanseg.kotlincommons.timedate.transformer.BoundedContext
import java.time.Instant
import java.time.temporal.ChronoUnit

class TimeScheduleContext(val boundedContext: BoundedContext, val frequency: UnitBasedTimeContainer, private val ceiled: Boolean = false): TimeContext {

    override val asHour: Long
        get() = boundedContext.asHour
    override val asMinute: Long
        get() = boundedContext.asMinute
    override val asSeconds: Long
        get() = boundedContext.asSeconds
    override val asNano: Long
        get() = boundedContext.asNano

    /**
     * Things to implement before finishing this method:
     *  We first must fully implement coercion. Coercion is used to calculate the frequency.
     */
    fun perform(callback: () -> Unit) {
        var tracker = 0L
        val freq = boundedContext.asSeconds / frequency.unit.value

        if(ceiled) {
            val start = Instant.now()
            val end = start.truncatedTo(frequency.unit.asChrono()).plusSeconds(frequency.unit.asSeconds)
            val diff = start.until(end, ChronoUnit.MILLIS)
            Thread.sleep(diff)
        }

        while(true) {
            callback()
            if(ceiled) {
                val start = Instant.now()
                val end = start.truncatedTo(frequency.unit.asChrono()).plusSeconds(frequency.unit.asSeconds)
                val diff = start.until(end, ChronoUnit.MILLIS)
                Thread.sleep(diff)
            }
            else
                Thread.sleep(frequency.unit.getTimeMillis())
            tracker++
            if(tracker >= freq)
                break
        }
    }

    fun performAsync(callback: () -> Unit) = Unit
}

infix fun BoundedContext.every(context: UnitBasedTimeContainer) = TimeScheduleContext(this, context)
infix fun BoundedContext.everyExact(context: UnitBasedTimeContainer) = TimeScheduleContext(this, context, true)