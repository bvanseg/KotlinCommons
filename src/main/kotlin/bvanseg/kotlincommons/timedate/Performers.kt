package bvanseg.kotlincommons.timedate

import bvanseg.kotlincommons.timedate.transformer.BoundedContext

class TimeScheduleContext(val boundedContext: BoundedContext, val frequency: UnitBasedTimeContainer): TimeContext {

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

        while(true) {
            callback()
            Thread.sleep(frequency.unit.getTimeMillis())
            tracker++
            if(tracker >= freq) // TODO: Figure out times to iterate from bounded context.
                break
        }
    }

    fun performAsync(callback: () -> Unit) = Unit
}

infix fun BoundedContext.every(context: UnitBasedTimeContainer) = TimeScheduleContext(this, context)