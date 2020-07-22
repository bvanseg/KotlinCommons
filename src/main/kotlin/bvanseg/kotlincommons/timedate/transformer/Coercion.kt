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
                UnitBasedTimeContainer(TimeContextUnit.Year(original.timeObject.year))
            TimeUnit.MONTH ->
                UnitBasedTimeContainer(TimeContextUnit.Month(original.timeObject.month))
            TimeUnit.DAY ->
                UnitBasedTimeContainer(TimeContextUnit.Day(original.timeObject.day))
            TimeUnit.HOUR ->
                UnitBasedTimeContainer(TimeContextUnit.Hour(original.asHour))
            TimeUnit.MINUTE ->
                UnitBasedTimeContainer(TimeContextUnit.Minute(original.asMinute))
            TimeUnit.SECOND ->
                UnitBasedTimeContainer(TimeContextUnit.Second(original.asSeconds))
            TimeUnit.MILLIS ->
                UnitBasedTimeContainer(TimeContextUnit.Millis(original.asMillis))
            TimeUnit.NANO ->
                UnitBasedTimeContainer(TimeContextUnit.Nano(original.asNano))
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

infix fun TimeContext.into(unit: TimeUnit): UnitBasedTimeContainer =
    when(this){
        is BoundedContext -> {
            when(unit){
                TimeUnit.YEAR ->
                    UnitBasedTimeContainer(TimeContextUnit.Year((this.right into years).timeObject.year - (this.left into years).timeObject.year))
                TimeUnit.MONTH ->
                    UnitBasedTimeContainer(TimeContextUnit.Month((this.right into months).timeObject.month - (this.left into months).timeObject.month))
                TimeUnit.DAY ->
                    UnitBasedTimeContainer(TimeContextUnit.Day((this.right into days).timeObject.day - (this.left into days).timeObject.day))
                TimeUnit.HOUR ->
                    UnitBasedTimeContainer(TimeContextUnit.Hour((this.right into hours).timeObject.hour - (this.left into hours).timeObject.hour))
                TimeUnit.MINUTE ->
                    UnitBasedTimeContainer(TimeContextUnit.Minute((this.right into minutes).timeObject.minute - (this.left into minutes).timeObject.minute))
                TimeUnit.SECOND ->
                    UnitBasedTimeContainer(TimeContextUnit.Second((this.right into seconds).timeObject.second - (this.left into seconds).timeObject.second))
                TimeUnit.MILLIS ->
                    UnitBasedTimeContainer(TimeContextUnit.Nano((this.right into millis).timeObject.millis - (this.left into millis).timeObject.nano))
                TimeUnit.NANO ->
                    UnitBasedTimeContainer(TimeContextUnit.Nano((this.right into nanos).timeObject.nano - (this.left into nanos).timeObject.nano))
            }
        }
        is TimeScheduleContext -> {
            when(unit){
                TimeUnit.YEAR ->
                    UnitBasedTimeContainer(TimeContextUnit.Year((this.boundedContext into years).timeObject.year))
                TimeUnit.MONTH ->
                    UnitBasedTimeContainer(TimeContextUnit.Month((this.boundedContext into months).timeObject.month))
                TimeUnit.DAY ->
                    UnitBasedTimeContainer(TimeContextUnit.Day((this.boundedContext into days).timeObject.day))
                TimeUnit.HOUR ->
                    UnitBasedTimeContainer(TimeContextUnit.Hour((this.boundedContext into hours).timeObject.hour))
                TimeUnit.MINUTE ->
                    UnitBasedTimeContainer(TimeContextUnit.Minute((this.boundedContext into minutes).timeObject.minute))
                TimeUnit.SECOND ->
                    UnitBasedTimeContainer(TimeContextUnit.Second((this.boundedContext into seconds).timeObject.second))
                TimeUnit.MILLIS ->
                    UnitBasedTimeContainer(TimeContextUnit.Nano((this.boundedContext into millis).timeObject.millis))
                TimeUnit.NANO ->
                    UnitBasedTimeContainer(TimeContextUnit.Nano((this.boundedContext into nanos).timeObject.nano))
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

infix fun LocalDateTimeContainer.into(unit: TimeUnit): UnitBasedTimeContainer{
    val here = this.toUnitBasedTimeContainer()
    return here into unit
}

infix fun UnitBasedTimeContainer.into(unit: TimeUnit): UnitBasedTimeContainer{
    val result = flatmap { _: Time ->
        val context = TimeCoercionContext(this, unit)
        context.coercedTime.timeObject
    } as UnitBasedTimeContainer
    return result.flatmap { _: TimeContextUnit ->
        when(unit){
            TimeUnit.NANO -> TimeContextUnit.Nano(result.timeObject.nano)
            TimeUnit.MILLIS -> TimeContextUnit.Millis(result.timeObject.millis)
            TimeUnit.SECOND -> TimeContextUnit.Second(result.timeObject.second)
            TimeUnit.MINUTE -> TimeContextUnit.Minute(result.timeObject.minute)
            TimeUnit.HOUR -> TimeContextUnit.Hour(result.timeObject.hour)
            TimeUnit.DAY -> TimeContextUnit.Day(result.timeObject.day)
            TimeUnit.MONTH -> TimeContextUnit.Month(result.timeObject.month)
            TimeUnit.YEAR -> TimeContextUnit.Year(result.timeObject.year)
        }
    } as UnitBasedTimeContainer
}