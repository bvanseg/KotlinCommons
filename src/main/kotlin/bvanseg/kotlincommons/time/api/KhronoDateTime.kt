package bvanseg.kotlincommons.time.api

import java.time.LocalDateTime

/**
 * @author Boston Vanseghi
 * @since 2.8.0
 */
open class KhronoDateTime(open val date: KhronoDate, open val time: KhronoTime): Comparable<KhronoDateTime> {

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

    open val day: Khrono
        get() = date.day
    open val month: Khrono
        get() = date.monthKhrono
    open val year: Khrono
        get() = date.year

    open val hour: Khrono
        get() = time.hour
    open val minute: Khrono
        get() = time.minute
    open val second: Khrono
        get() = time.second
    open val millisecond: Khrono
        get() = time.millisecond
    open val microsecond: Khrono
        get() = time.microsecond
    open val nanosecond: Khrono
        get() = time.nanosecond

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
        date.year.toInt(),
        date.month.monthValue,
        date.day.toInt(),
        time.hour.toInt(),
        time.minute.toInt(),
        time.second.toInt(),
        time.millisecond.toNanos().toInt() + time.microsecond.toNanos().toInt() + time.nanosecond.value.toInt()
    )

    fun isBefore(lowerBound: KhronoDateTime): Boolean = this.asNanos < lowerBound.asNanos
    fun isBeforeOrAt(lowerBound: KhronoDateTime): Boolean = this.asNanos <= lowerBound.asNanos

    fun isAfter(lowerBound: KhronoDateTime): Boolean = this.asNanos > lowerBound.asNanos
    fun isAtOrAfter(lowerBound: KhronoDateTime): Boolean = this.asNanos >= lowerBound.asNanos

    fun toMutable(): MutableKhronoDateTime = MutableKhronoDateTime(date.toMutable(), time.toMutable())

    override fun toString(): String = "$date-$time"

    override fun equals(other: Any?): Boolean {
        if (other !is KhronoDateTime) {
            return false
        }

        if (this.date != other.date) return false
        if (this.time != other.time) return false

        return true
    }

    override fun compareTo(other: KhronoDateTime): Int = when {
        this.asNanos < other.asNanos -> -1
        this.asNanos > other.asNanos -> 1
        else -> 0
    }

    companion object {
        fun yesterday(): KhronoDateTime = now().toMutable().apply { day -= 1 }
        fun now(): KhronoDateTime = LocalDateTime.now().toKhronoDateTime()
        fun tomorrow(): KhronoDateTime = now().toMutable().apply { day += 1 }

        fun afternoon(): KhronoDateTime = KhronoDateTime(KhronoDate.now(), KhronoTime(hr = 12.0))
        fun midnight(): KhronoDateTime = KhronoDateTime(KhronoDate.now(), KhronoTime())
    }
}