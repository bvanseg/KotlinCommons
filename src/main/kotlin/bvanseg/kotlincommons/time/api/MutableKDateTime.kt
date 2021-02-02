package bvanseg.kotlincommons.time.api

/**
 * @author Boston Vanseghi
 * @since 2.8.0
 */
class MutableKDateTime(override val date: MutableKDate, override val time: MutableKTime): KDateTime(date, time) {

    constructor(
        day: Double,
        month: Int,
        year: Double
    ) : this(MutableKDate(day, month, year), MutableKTime())

    constructor(
        day: Double,
        month: Int,
        year: Double,
        hour: Double = 0.0,
        minute: Double = 0.0,
        second: Double = 0.0,
        millisecond: Double = 0.0,
        microsecond: Double = 0.0,
        nanosecond: Double = 0.0
    ) : this(MutableKDate(day, month, year), MutableKTime(hour, minute, second, millisecond, microsecond, nanosecond))

    constructor(
        day: Double,
        month: KMonth,
        year: Double,
        hour: Double = 0.0,
        minute: Double = 0.0,
        second: Double = 0.0,
        millisecond: Double = 0.0,
        microsecond: Double = 0.0,
        nanosecond: Double = 0.0
    ) : this(MutableKDate(day, month.monthValue, year), MutableKTime(hour, minute, second, millisecond, microsecond, nanosecond))
}