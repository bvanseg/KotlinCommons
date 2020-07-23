package bvanseg.kotlincommons.timedate.transformer

import bvanseg.kotlincommons.timedate.*

class TimeCoercionContext(
    val original: TimeContainer,
    val unit: TimeUnit
): TimeTransformerContext
by DefaultTimePerformer(original){
    /**
     * Coerce [original] into [unit] such that [original] is converted to [unit], e.g.:
     * 12/21/2020/04:20:30.5349 -> Seconds -> year.asSeconds + month.asSeconds + day.asSeconds + hour.asSeconds + minute.asSeconds + second
     *
     * This should be done via a [fold] operation. Should be implemented as a method on [TimeContainer]
     *
     * Covariant Coercion :: Greater units are converted to seconds and folded together until you've folded to [unit] then convert the seconds to [unit]
     *  e.g.: Greater units are Year, Month, Day, which have contravariant coercion strategies
     * Contravariant Coercion :: Lesser units are zero'd out
     *  e.g.: Lesser units are Hour, Minute, Second, Nanosecond which have covariant coercion strategies
     * Invariant Coercion :: Just the [unit]'s place is zero'd out aka truncate
     *  e.g.: truncate is invariant, therefore it has no relation to any unit or strategy
     *
     * @Note Should we fold years into the lesser units?
     */
    val coercedTime =
        when(unit){
            TimeUnit.YEAR ->
                TimeContextUnit.Year(original.timeObject.year)
            TimeUnit.MONTH ->
                TimeContextUnit.Month(original.timeObject.month)
            TimeUnit.DAY ->
                TimeContextUnit.Day(original.timeObject.day)
            TimeUnit.HOUR ->
                TimeContextUnit.Hour(original.asHour)
            TimeUnit.MINUTE ->
                TimeContextUnit.Minute(original.asMinute)
            TimeUnit.SECOND ->
                TimeContextUnit.Second(original.asSeconds)
            TimeUnit.MILLIS ->
                TimeContextUnit.Millis(original.asMillis)
            TimeUnit.NANO ->
                TimeContextUnit.Nano(original.asNano)
        }
    override val asHour: Long
        get() = original.asHour
    override val asMinute: Long
        get() = original.asMinute
    override val asSeconds: Long
        get() = original.asSeconds
    override val asNano: Long
        get() = original.asNano
    override val asMillis: Long
        get() = original.asMillis

}

infix fun TimeContext.into(unit: TimeUnit): TimeContextUnit =
    when(this){
        is BoundedContext -> {
            when(unit){
                TimeUnit.YEAR ->
                    TimeContextUnit.Year((this.right into years).value - (this.left into years).value)
                TimeUnit.MONTH ->
                    TimeContextUnit.Month((this.right into months).value - (this.left into months).value)
                TimeUnit.DAY ->
                    TimeContextUnit.Day((this.right into days).value - (this.left into days).value)
                TimeUnit.HOUR ->
                    TimeContextUnit.Hour((this.right into hours).value - (this.left into hours).value)
                TimeUnit.MINUTE ->
                    TimeContextUnit.Minute((this.right into minutes).value - (this.left into minutes).value)
                TimeUnit.SECOND ->
                    TimeContextUnit.Second((this.right into seconds).value - (this.left into seconds).value)
                TimeUnit.MILLIS ->
                    TimeContextUnit.Nano((this.right into millis).value - (this.left into millis).value)
                TimeUnit.NANO ->
                    TimeContextUnit.Nano((this.right into nanos).value - (this.left into nanos).value)
            }
        }
        is TimeScheduleContext -> {
            when(unit){
                TimeUnit.YEAR ->
                    TimeContextUnit.Year((this.boundedContext into years).value)
                TimeUnit.MONTH ->
                    TimeContextUnit.Month((this.boundedContext into months).value)
                TimeUnit.DAY ->
                    TimeContextUnit.Day((this.boundedContext into days).value)
                TimeUnit.HOUR ->
                    TimeContextUnit.Hour((this.boundedContext into hours).value)
                TimeUnit.MINUTE ->
                    TimeContextUnit.Minute((this.boundedContext into minutes).value)
                TimeUnit.SECOND ->
                    TimeContextUnit.Second((this.boundedContext into seconds).value)
                TimeUnit.MILLIS ->
                    TimeContextUnit.Nano((this.boundedContext into millis).value)
                TimeUnit.NANO ->
                    TimeContextUnit.Nano((this.boundedContext into nanos).value)
            }
        }
        is LocalDateTimeContainer ->
            when(unit){
                TimeUnit.YEAR -> this into years
                TimeUnit.MONTH -> this into months
                TimeUnit.DAY -> this into days
                TimeUnit.HOUR -> this into hours
                TimeUnit.MINUTE -> this into minutes
                TimeUnit.SECOND -> this into seconds
                TimeUnit.MILLIS -> this into millis
                TimeUnit.NANO -> this into nanos
            }
        is UnitBasedTimeContainer ->
            when(unit){
                TimeUnit.YEAR -> this into years
                TimeUnit.MONTH -> this into months
                TimeUnit.DAY -> this into days
                TimeUnit.HOUR -> this into hours
                TimeUnit.MINUTE -> this into minutes
                TimeUnit.SECOND -> this into seconds
                TimeUnit.MILLIS -> this into millis
                TimeUnit.NANO -> this into nanos
            }
        is TimePerformer ->
            when(unit){
                TimeUnit.YEAR -> inner into years
                TimeUnit.MONTH -> inner into months
                TimeUnit.DAY -> inner into days
                TimeUnit.HOUR -> inner into hours
                TimeUnit.MINUTE -> inner into minutes
                TimeUnit.SECOND -> inner into seconds
                TimeUnit.MILLIS -> inner into millis
                TimeUnit.NANO -> inner into nanos
            }
        else -> TODO("Will fill in other stuff later :)")
    }

infix fun LocalDateTimeContainer.into(unit: TimeUnit): TimeContextUnit{
    val here = this.toUnitBasedTimeContainer()
    return here into unit
}

infix fun UnitBasedTimeContainer.into(unit: TimeUnit): TimeContextUnit{
    val context = TimeCoercionContext(this, unit)
    val result = context.coercedTime
    return when(unit){
        TimeUnit.NANO -> TimeContextUnit.Nano(result.value)
        TimeUnit.MILLIS -> TimeContextUnit.Millis(result.value)
        TimeUnit.SECOND -> TimeContextUnit.Second(result.value)
        TimeUnit.MINUTE -> TimeContextUnit.Minute(result.value)
        TimeUnit.HOUR -> TimeContextUnit.Hour(result.value)
        TimeUnit.DAY -> TimeContextUnit.Day(result.value)
        TimeUnit.MONTH -> TimeContextUnit.Month(result.value)
        TimeUnit.YEAR -> TimeContextUnit.Year(result.value)
    }
}