package bvanseg.kotlincommons.time.api

/**
 * @author Boston Vanseghi
 * @since 2.8.0
 */
class MutableKhronoDateTime(override val date: MutableKhronoDate, override val time: MutableKhronoTime): KhronoDateTime(date, time) {

    constructor(
        day: Double,
        month: Int,
        year: Double
    ) : this(MutableKhronoDate(day, month, year), MutableKhronoTime())

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
    ) : this(MutableKhronoDate(day, month, year), MutableKhronoTime(hour, minute, second, millisecond, microsecond, nanosecond))

    constructor(
        day: Double,
        month: KhronoMonth,
        year: Double,
        hour: Double = 0.0,
        minute: Double = 0.0,
        second: Double = 0.0,
        millisecond: Double = 0.0,
        microsecond: Double = 0.0,
        nanosecond: Double = 0.0
    ) : this(MutableKhronoDate(day, month.monthValue, year), MutableKhronoTime(hour, minute, second, millisecond, microsecond, nanosecond))
}