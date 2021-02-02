package bvanseg.kotlincommons.time.api

/**
 * @author Boston Vanseghi
 * @since 2.8.0
 */
class MutableKhronoDateTime(override val date: MutableKhronoDate, override val time: MutableKhronoTime) :
    KhronoDateTime(date, time) {

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
    ) : this(
        MutableKhronoDate(day, month, year),
        MutableKhronoTime(hour, minute, second, millisecond, microsecond, nanosecond)
    )

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
    ) : this(
        MutableKhronoDate(day, month.monthValue, year),
        MutableKhronoTime(hour, minute, second, millisecond, microsecond, nanosecond)
    )

    override val asNanos: Double
        get() = date.asNanos + time.asNanos
    override val asMicros: Double
        get() = date.asMicros + time.asMicros
    override val asMillis: Double
        get() = date.asMillis + time.asMillis
    override val asSeconds: Double
        get() = date.asSeconds + time.asSeconds
    override val asMinutes: Double
        get() = date.asMinutes + time.asMinutes
    override val asHours: Double
        get() = date.asHours + time.asHours
    override val asHalfDays: Double
        get() = date.asHalfDays + time.asHalfDays
    override val asDays: Double
        get() = date.asDays + time.asDays
    override val asWeeks: Double
        get() = date.asWeeks + time.asWeeks
    override val asYears: Double
        get() = date.asYears + time.asYears
    override val asDecades: Double
        get() = date.asDecades + time.asDecades
    override val asCenturies: Double
        get() = date.asCenturies + time.asCenturies
    override val asMillenniums: Double
        get() = date.asMillenniums + time.asMillenniums
}