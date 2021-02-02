package bvanseg.kotlincommons.time.api

/**
 * @author Boston Vanseghi
 * @since 2.8.0
 */
// TODO: Add support for AM/PM.
// TODO: Add support for timezones.
// TODO: Double-check whether or not negatives work.
class KTime(
    hr: Double = 0.0,
    min: Double = 0.0,
    sec: Double = 0.0,
    millis: Double = 0.0,
    micro: Double = 0.0,
    nano: Double = 0.0
) {
    val nanosecond: Khrono
    val microsecond: Khrono
    val millisecond: Khrono
    val second: Khrono
    val minute: Khrono
    val hour: Khrono

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

    val asNanos: Double by lazy { Khrono.combineAll(KhronoUnit.NANOSECOND, hour, minute, second, millisecond, microsecond, nanosecond).value }
    val asMicros: Double by lazy { Khrono.combineAll(KhronoUnit.MICROSECOND, hour, minute, second, millisecond, microsecond, nanosecond).value }
    val asMillis: Double by lazy { Khrono.combineAll(KhronoUnit.MILLISECOND, hour, minute, second, millisecond, microsecond, nanosecond).value }
    val asSeconds: Double by lazy { Khrono.combineAll(KhronoUnit.SECOND, hour, minute, second, millisecond, microsecond, nanosecond).value }
    val asMinutes: Double by lazy { Khrono.combineAll(KhronoUnit.MINUTE, hour, minute, second, millisecond, microsecond, nanosecond).value }
    val asHours: Double by lazy { Khrono.combineAll(KhronoUnit.HOUR, hour, minute, second, millisecond, microsecond, nanosecond).value }
}