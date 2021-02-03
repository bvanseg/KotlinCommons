package bvanseg.kotlincommons.time.api

import java.time.LocalTime
import bvanseg.kotlincommons.time.api.operator.plusAssign

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
    override var nanosecond: MutableKhrono = MutableKhrono.EMPTY
        private set
    override var microsecond: MutableKhrono = MutableKhrono.EMPTY
        private set
    override var millisecond: MutableKhrono = MutableKhrono.EMPTY
        private set
    override var second: MutableKhrono = MutableKhrono.EMPTY
        private set
    override var minute: MutableKhrono = MutableKhrono.EMPTY
        private set
    override var hour: MutableKhrono = MutableKhrono.EMPTY
        private set

    init {
        val nanoLeftover = nano % KhronoUnit.NANOSECOND.max
        val nanoOverflow = (nano - nanoLeftover)
        val microFromNano = nanoOverflow / KhronoUnit.NANOSECOND.max
        nanosecond = MutableKhrono(nanoLeftover, KhronoUnit.NANOSECOND)
        nanosecond.onChange = {
            updateCalculations()
            handleOverflow()
        }

        val microTotal = micro + microFromNano

        val microLeftover = microTotal % KhronoUnit.MICROSECOND.max
        val microOverflow = (microTotal - microLeftover)
        val millisFromMicro = microOverflow / KhronoUnit.MICROSECOND.max
        microsecond = MutableKhrono(microLeftover, KhronoUnit.MICROSECOND)
        microsecond.onChange = {
            updateCalculations()
            handleOverflow()
        }

        val millisTotal = millis + millisFromMicro

        val millisLeftover = millisTotal % KhronoUnit.MILLISECOND.max
        val millisOverflow = (millisTotal - millisLeftover)
        val secFromMillis = millisOverflow / KhronoUnit.MILLISECOND.max
        millisecond = MutableKhrono(millisLeftover, KhronoUnit.MILLISECOND)
        millisecond.onChange = {
            updateCalculations()
            handleOverflow()
        }

        val secTotal = sec + secFromMillis

        val secLeftover = secTotal % KhronoUnit.SECOND.max
        val secOverflow = (secTotal - secLeftover)
        val minFromSec = secOverflow / KhronoUnit.SECOND.max
        second = MutableKhrono(secLeftover, KhronoUnit.SECOND)
        second.onChange = {
            updateCalculations()
            handleOverflow()
        }

        val minTotal = min + minFromSec

        val minLeftover = minTotal % KhronoUnit.MINUTE.max
        val minOverflow = (minTotal - minLeftover)
        val hourFromMin = minOverflow / KhronoUnit.MINUTE.max
        minute = MutableKhrono(minLeftover, KhronoUnit.MINUTE)
        minute.onChange = {
            updateCalculations()
            handleOverflow()
        }

        val hourTotal = hr + hourFromMin

        val hourLeftover = hourTotal % KhronoUnit.HOUR.max
        hour = MutableKhrono(hourLeftover, KhronoUnit.HOUR)
        hour.onChange = {
            updateCalculations()
            handleOverflow()
        }
    }

    override var asNanos: Double = Khrono.combineAll(
        KhronoUnit.NANOSECOND,
        hour,
        minute,
        second,
        millisecond,
        microsecond,
        nanosecond
    ).value
        private set

    private var nanoObject = Khrono(asNanos, KhronoUnit.NANOSECOND)

    override var asMicros: Double = nanoObject.toMicros().toDouble()
        private set
    override var asMillis: Double = nanoObject.toMillis().toDouble()
        private set
    override var asSeconds: Double = nanoObject.toSeconds().toDouble()
        private set
    override var asMinutes: Double = nanoObject.toMinutes().toDouble()
        private set
    override var asHours: Double = nanoObject.toHours().toDouble()
        private set
    override var asHalfDays: Double = nanoObject.toHalfDays().toDouble()
        private set
    override var asDays: Double = nanoObject.toDays().toDouble()
        private set
    override var asWeeks: Double = nanoObject.toWeeks().toDouble()
        private set
    override var asYears: Double = nanoObject.toYears().toDouble()
        private set
    override var asDecades: Double = nanoObject.toDecades().toDouble()
        private set
    override var asCenturies: Double = nanoObject.toCenturies().toDouble()
        private set
    override var asMillenniums: Double = nanoObject.toMillenniums().toDouble()
        private set

    private fun updateCalculations() {
        asNanos = Khrono.combineAll(
            KhronoUnit.NANOSECOND,
            hour,
            minute,
            second,
            millisecond,
            microsecond,
            nanosecond
        ).value

        nanoObject = asNanos.nanoseconds

        asMicros = nanoObject.toMicros().toDouble()
        asMillis = nanoObject.toMillis().toDouble()
        asSeconds = nanoObject.toSeconds().toDouble()
        asMinutes = nanoObject.toMinutes().toDouble()
        asHours = nanoObject.toHours().toDouble()
        asHalfDays = nanoObject.toHalfDays().toDouble()
        asDays = nanoObject.toDays().toDouble()
        asWeeks = nanoObject.toWeeks().toDouble()
        asYears = nanoObject.toYears().toDouble()
        asDecades = nanoObject.toDecades().toDouble()
        asCenturies = nanoObject.toCenturies().toDouble()
        asMillenniums = nanoObject.toMillenniums().toDouble()
    }

    private fun clearCallbacks() {
        nanosecond.onChange = null
        microsecond.onChange = null
        millisecond.onChange = null
        second.onChange = null
        minute.onChange = null
        hour.onChange = null
    }

    private fun handleOverflow() {
        // We need to temporarily store and clear callbacks to avoid a stack overflow due to circular self-referencing.
        val nanoCallback = nanosecond.onChange
        val microCallback = microsecond.onChange
        val millisCallback = millisecond.onChange
        val secondCallback = second.onChange
        val minuteCallback = minute.onChange
        val hourCallback = hour.onChange

        clearCallbacks()

        val nanoLeftover = nanosecond.value % KhronoUnit.NANOSECOND.max
        val nanoOverflow = (nanosecond.value - nanoLeftover)
        val microFromNano = nanoOverflow / KhronoUnit.NANOSECOND.max
        nanosecond.value = nanoLeftover

        val microTotal = microsecond.value + microFromNano

        val microLeftover = microTotal % KhronoUnit.MICROSECOND.max
        val microOverflow = (microTotal - microLeftover)
        val millisFromMicro = microOverflow / KhronoUnit.MICROSECOND.max
        microsecond.value = microLeftover

        val millisTotal = millisecond.value + millisFromMicro

        val millisLeftover = millisTotal % KhronoUnit.MILLISECOND.max
        val millisOverflow = (millisTotal - millisLeftover)
        val secFromMillis = millisOverflow / KhronoUnit.MILLISECOND.max
        millisecond.value = millisLeftover

        val secTotal = second.value + secFromMillis

        val secLeftover = secTotal % KhronoUnit.SECOND.max
        val secOverflow = (secTotal - secLeftover)
        val minFromSec = secOverflow / KhronoUnit.SECOND.max
        second.value = secLeftover

        val minTotal = minute.value + minFromSec

        val minLeftover = minTotal % KhronoUnit.MINUTE.max
        val minOverflow = (minTotal - minLeftover)
        val hourFromMin = minOverflow / KhronoUnit.MINUTE.max
        minute.value = minLeftover

        val hourTotal = hour.value + hourFromMin

        val hourLeftover = hourTotal % KhronoUnit.HOUR.max
        hour.value = hourLeftover

        nanosecond.onChange = nanoCallback
        microsecond.onChange = microCallback
        millisecond.onChange = millisCallback
        second.onChange = secondCallback
        minute.onChange = minuteCallback
        hour.onChange = hourCallback
    }

    fun clear() {
        nanosecond.value = 0.0
        microsecond.value = 0.0
        millisecond.value = 0.0
        second.value = 0.0
        minute.value = 0.0
        hour.value = 0.0
    }

    companion object {
        fun now(): MutableKhronoTime = LocalTime.now().toMutableKhronoTime()
        val midnight: KhronoTime = KhronoTime(hr = 0.0)
        val afternoon: KhronoTime = KhronoTime(hr = 12.0)
    }
}