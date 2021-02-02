package bvanseg.kotlincommons.time.api.operator

import bvanseg.kotlincommons.time.api.KDate
import bvanseg.kotlincommons.time.api.KDateTime
import bvanseg.kotlincommons.time.api.KTime
import bvanseg.kotlincommons.time.api.Khrono
import bvanseg.kotlincommons.time.api.KhronoUnit

infix fun Khrono.into(unit: KhronoUnit): Khrono {
    val newValue = this.unit.convertTo(this.value, unit)
    return Khrono(newValue, unit)
}

operator fun KDate.plus(other: KTime) = KDateTime(this, other)
operator fun KTime.plus(other: KDate) = KDateTime(other, this)