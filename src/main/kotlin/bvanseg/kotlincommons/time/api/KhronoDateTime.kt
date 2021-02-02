package bvanseg.kotlincommons.time.api

import java.time.LocalDateTime

/**
 * @author Boston Vanseghi
 * @since 2.8.0
 */
open class KhronoDateTime(open val date: KhronoDate, open val time: KhronoTime) {

    constructor(
        day: Double,
        month: Int,
        year: Double
    ) : this(KhronoDate(day, month, year), KhronoTime())

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
    ) : this(KhronoDate(day, month, year), KhronoTime(hour, minute, second, millisecond, microsecond, nanosecond))

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
        KhronoDate(day, month.monthValue, year),
        KhronoTime(hour, minute, second, millisecond, microsecond, nanosecond)
    )

    open val asNanos: Double by lazy { date.asNanos + time.asNanos }
    open val asMicros: Double by lazy { date.asMicros + time.asMicros }
    open val asMillis: Double by lazy { date.asMillis + time.asMillis }
    open val asSeconds: Double by lazy { date.asSeconds + time.asSeconds }
    open val asMinutes: Double by lazy { date.asMinutes + time.asMinutes }
    open val asHours: Double by lazy { date.asHours + time.asHours }
    open val asHalfDays: Double by lazy { date.asHalfDays + time.asHalfDays }
    open val asDays: Double by lazy { date.asDays + time.asDays }
    open val asWeeks: Double by lazy { date.asWeeks + time.asWeeks }
    open val asYears: Double by lazy { date.asYears + time.asYears }
    open val asDecades: Double by lazy { date.asDecades + time.asDecades }
    open val asCenturies: Double by lazy { date.asCenturies + time.asCenturies }
    open val asMillenniums: Double by lazy { date.asMillenniums + time.asMillenniums }

    fun toLocalDateTime(): LocalDateTime = LocalDateTime.of(
        date.year.value.toInt(),
        date.month.monthValue,
        date.day.value.toInt(),
        time.hour.value.toInt(),
        time.minute.value.toInt(),
        time.second.value.toInt(),
        time.millisecond.toNanos().toInt() + time.microsecond.toNanos().toInt() + time.nanosecond.value.toInt()
    )

    override fun toString(): String = "$date-$time"

    companion object {
        fun now(): KhronoDateTime = LocalDateTime.now().toKhronoDateTime()
    }
}