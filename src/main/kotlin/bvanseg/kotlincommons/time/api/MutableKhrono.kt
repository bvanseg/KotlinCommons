package bvanseg.kotlincommons.time.api

/**
 * @author Boston Vanseghi
 * @since 2.8.0
 */
data class MutableKhrono(override var value: Double, override var unit: KhronoUnit) : Khrono(value, unit)
