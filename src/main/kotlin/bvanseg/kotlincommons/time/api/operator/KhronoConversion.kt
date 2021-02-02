package bvanseg.kotlincommons.time.api.operator

import bvanseg.kotlincommons.time.api.Khrono
import bvanseg.kotlincommons.time.api.KhronoDate
import bvanseg.kotlincommons.time.api.KhronoDateTime
import bvanseg.kotlincommons.time.api.KhronoTime
import bvanseg.kotlincommons.time.api.KhronoUnit

infix fun Khrono.into(unit: KhronoUnit): Khrono {
    val newValue = this.unit.convertTo(this.value, unit)
    return Khrono(newValue, unit)
}

operator fun KhronoDate.plus(other: KhronoTime) = KhronoDateTime(this, other)
operator fun KhronoTime.plus(other: KhronoDate) = KhronoDateTime(other, this)