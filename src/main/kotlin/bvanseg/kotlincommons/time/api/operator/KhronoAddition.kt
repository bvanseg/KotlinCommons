package bvanseg.kotlincommons.time.api.operator

import bvanseg.kotlincommons.time.api.Khrono
import bvanseg.kotlincommons.time.api.MutableKhrono

/*
    IMMUTABLE
 */
operator fun Khrono.inc(): MutableKhrono = this.toMutable().apply { this.value += 1 }

operator fun Khrono.plus(other: Khrono): Khrono {
    val comparable = this.unit.compareTo(other.unit)

    return when {
        // This context has the greater time unit. Convert down to other.
        comparable > 0 -> {
            Khrono(this.convertTo(other.unit) + other.value, other.unit)
        }
        comparable < 0 -> {
            Khrono(other.convertTo(this.unit) + this.value, this.unit)
        }
        else -> {
            Khrono(this.value + other.value, this.unit)
        }
    }
}

operator fun Khrono.plus(other: Number): Khrono = Khrono(this.value + other.toDouble(), this.unit)

/*
    MUTABLE
 */
operator fun MutableKhrono.inc(): MutableKhrono = this.apply { this.value += 1 }

operator fun MutableKhrono.plus(other: Number): MutableKhrono = this.apply { this.value += other.toDouble() }

operator fun MutableKhrono.plus(other: MutableKhrono): MutableKhrono {
    val comparable = this.unit.compareTo(other.unit)

    return when {
        // This context has the greater time unit. Convert down to other.
        comparable > 0 -> {
            MutableKhrono(this.convertTo(other.unit) + other.value, other.unit)
        }
        comparable < 0 -> {
            MutableKhrono(other.convertTo(this.unit) + this.value, this.unit)
        }
        else -> {
            MutableKhrono(this.value + other.value, this.unit)
        }
    }
}

operator fun MutableKhrono.plusAssign(other: Number) {
    this.apply { this.value += other.toDouble() }
}

