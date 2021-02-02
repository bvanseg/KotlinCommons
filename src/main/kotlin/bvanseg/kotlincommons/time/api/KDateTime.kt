package bvanseg.kotlincommons.time.api

/**
 * @author Boston Vanseghi
 * @since 2.8.0
 */
class KDateTime(val date: KDate, val time: KTime) {

    constructor(
        day: Double,
        month: Int,
        year: Double
    ) : this(KDate(day, month, year), KTime())

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
    ) : this(KDate(day, month, year), KTime(hour, minute, second, millisecond, microsecond, nanosecond))

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
    ) : this(KDate(day, month.monthValue, year), KTime(hour, minute, second, millisecond, microsecond, nanosecond))

    val asNanos: Double by lazy { date.asNanos + time.asNanos }
    val asMicros: Double by lazy { date.asMicros + time.asMicros }
    val asMillis: Double by lazy { date.asMillis + time.asMillis }
    val asSeconds: Double by lazy { date.asSeconds + time.asSeconds }
    val asMinutes: Double by lazy { date.asMinutes + time.asMinutes }
    val asHours: Double by lazy { date.asHours + time.asHours }
    val asHalfDays: Double by lazy { date.asHalfDays + time.asHalfDays }
    val asDays: Double by lazy { date.asDays + time.asDays }
    val asWeeks: Double by lazy { date.asWeeks + time.asWeeks }
    val asYears: Double by lazy { date.asYears + time.asYears }
    val asDecades: Double by lazy { date.asDecades + time.asDecades }
    val asCenturies: Double by lazy { date.asCenturies + time.asCenturies }
    val asMillenniums: Double by lazy { date.asMillenniums + time.asMillenniums }
}