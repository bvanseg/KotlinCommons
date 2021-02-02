package bvanseg.kotlincommons.time.api

/**
 * @author Boston Vanseghi
 * @since 2.8.0
 */
class MutableKhronoTime(
    hr: Double = 0.0,
    min: Double = 0.0,
    sec: Double = 0.0,
    millis: Double = 0.0,
    micro: Double = 0.0,
    nano: Double = 0.0
) : KhronoTime(hr, min, sec, millis, micro, nano) {
    override lateinit var nanosecond: Khrono
    override lateinit var microsecond: Khrono
    override lateinit var millisecond: Khrono
    override lateinit var second: Khrono
    override lateinit var minute: Khrono
    override lateinit var hour: Khrono

    init {
        val nanoLeftover = nano % KhronoUnit.NANOSECOND.max
        val nanoOverflow = (nano - nanoLeftover)
        val microFromNano = nanoOverflow / KhronoUnit.NANOSECOND.max
        nanosecond = Khrono(nanoLeftover, KhronoUnit.NANOSECOND)

        val microTotal = micro + microFromNano

        val microLeftover = microTotal % KhronoUnit.MICROSECOND.max
        val microOverflow = (microTotal - microLeftover)
        val millisFromMicro = microOverflow / KhronoUnit.MICROSECOND.max
        microsecond = Khrono(microLeftover, KhronoUnit.MICROSECOND)

        val millisTotal = millis + millisFromMicro

        val millisLeftover = millisTotal % KhronoUnit.MILLISECOND.max
        val millisOverflow = (millisTotal - millisLeftover)
        val secFromMillis = millisOverflow / KhronoUnit.MILLISECOND.max
        millisecond = Khrono(millisLeftover, KhronoUnit.MILLISECOND)

        val secTotal = sec + secFromMillis

        val secLeftover = secTotal % KhronoUnit.SECOND.max
        val secOverflow = (secTotal - secLeftover)
        val minFromSec = secOverflow / KhronoUnit.SECOND.max
        second = Khrono(secLeftover, KhronoUnit.SECOND)

        val minTotal = min + minFromSec

        val minLeftover = minTotal % KhronoUnit.MINUTE.max
        val minOverflow = (minTotal - minLeftover)
        val hourFromMin = minOverflow / KhronoUnit.MINUTE.max
        minute = Khrono(minLeftover, KhronoUnit.MINUTE)

        val hourTotal = hr + hourFromMin

        val hourLeftover = hourTotal % KhronoUnit.HOUR.max
        hour = Khrono(hourLeftover, KhronoUnit.HOUR)
    }

    override val asNanos: Double
        get() = Khrono.combineAll(KhronoUnit.NANOSECOND, hour, minute, second, millisecond, microsecond, nanosecond).value
    override val asMicros: Double
        get() = Khrono.combineAll(KhronoUnit.MICROSECOND, hour, minute, second, millisecond, microsecond, nanosecond).value
    override val asMillis: Double
        get() = Khrono.combineAll(KhronoUnit.MILLISECOND, hour, minute, second, millisecond, microsecond, nanosecond).value
    override val asSeconds: Double
        get() = Khrono.combineAll(KhronoUnit.SECOND, hour, minute, second, millisecond, microsecond, nanosecond).value
    override val asMinutes: Double
        get() = Khrono.combineAll(KhronoUnit.MINUTE, hour, minute, second, millisecond, microsecond, nanosecond).value
    override val asHours: Double
        get() = Khrono.combineAll(KhronoUnit.HOUR, hour, minute, second, millisecond, microsecond, nanosecond).value
    override val asHalfDays: Double
        get() = Khrono.combineAll(KhronoUnit.HALF_DAY, hour, minute, second, millisecond, microsecond, nanosecond).value
    override val asDays: Double
        get() = Khrono.combineAll(KhronoUnit.DAY, hour, minute, second, millisecond, microsecond, nanosecond).value
    override val asWeeks: Double
        get() = Khrono.combineAll(KhronoUnit.WEEK, hour, minute, second, millisecond, microsecond, nanosecond).value
    override val asYears: Double
        get() = Khrono.combineAll(KhronoUnit.YEAR, hour, minute, second, millisecond, microsecond, nanosecond).value
    override val asDecades: Double
        get() = Khrono.combineAll(KhronoUnit.DECADE, hour, minute, second, millisecond, microsecond, nanosecond).value
    override val asCenturies: Double
        get() = Khrono.combineAll(KhronoUnit.CENTURY, hour, minute, second, millisecond, microsecond, nanosecond).value
    override val asMillenniums: Double
        get() = Khrono.combineAll(KhronoUnit.MILLENNIUM, hour, minute, second, millisecond, microsecond, nanosecond).value
}