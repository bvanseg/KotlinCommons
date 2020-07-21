package bvanseg.kotlincommons.timedate.transformer

import bvanseg.kotlincommons.timedate.*

class TimeCoercionContext(val original: TimeContainer, val unit: TimeContextUnit): TimeTransformerContext{
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
            is TimeContextUnit.Year ->
                UnitBasedTimeContainer(TimeContextUnit.Year(original.timeObject.year))
            is TimeContextUnit.Month ->
                UnitBasedTimeContainer(TimeContextUnit.Month(original.timeObject.month))
            is TimeContextUnit.Day ->
                UnitBasedTimeContainer(TimeContextUnit.Day(original.timeObject.day))
            is TimeContextUnit.Hour ->
                UnitBasedTimeContainer(TimeContextUnit.Hour(original.asHour))
            is TimeContextUnit.Minute ->
                UnitBasedTimeContainer(TimeContextUnit.Hour(original.asMinute))
            is TimeContextUnit.Second ->
                UnitBasedTimeContainer(TimeContextUnit.Hour(original.asSeconds))
            is TimeContextUnit.Nano ->
                UnitBasedTimeContainer(TimeContextUnit.Hour(original.asNano))
            is TimeContextUnit.Week -> original
            is TimeContextUnit.None -> original
        }
    override val asHour: Long
        get() = original.asHour
    override val asMinute: Long
        get() = original.asMinute
    override val asSeconds: Long
        get() = original.asSeconds
    override val asNano: Long
        get() = original.asNano

}