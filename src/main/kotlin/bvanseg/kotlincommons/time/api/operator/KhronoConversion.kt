package bvanseg.kotlincommons.time.api.operator

import bvanseg.kotlincommons.time.api.Khrono
import bvanseg.kotlincommons.time.api.KhronoUnit

infix fun Khrono.into(unit: KhronoUnit): Khrono {
    val newValue = this.unit.convertTo(this.value, unit)
    return Khrono(newValue, unit)
}