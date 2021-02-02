package bvanseg.kotlincommons.time.api.operator

import bvanseg.kotlincommons.time.api.Khrono
import bvanseg.kotlincommons.time.api.MutableKhrono

/*
    IMMUTABLE
 */
operator fun Khrono.dec(): Khrono = this.toMutable().apply { this.value -= 1 }

operator fun Khrono.minus(other: Khrono): Khrono {
    val comparable = this.unit.compareTo(other.unit)

    return when {
        // This context has the greater time unit. Convert down to other.
        comparable > 0 -> {
            Khrono(this.convertTo(other.unit) - other.value, other.unit)
        }
        comparable < 0 -> {
            Khrono(other.convertTo(this.unit) - this.value, this.unit)
        }
        else -> {
            Khrono(this.value - other.value, this.unit)
        }
    }
}

/*
    MUTABLE
 */
operator fun MutableKhrono.dec(): MutableKhrono = this.apply { this.value -= 1 }

operator fun MutableKhrono.minus(other: Number): MutableKhrono = this.apply { this.value -= other.toDouble() }

operator fun MutableKhrono.minus(other: Khrono): MutableKhrono {
    val comparable = this.unit.compareTo(other.unit)

    return when {
        // This context has the greater time unit. Convert down to other.
        comparable > 0 -> {
            MutableKhrono(this.convertTo(other.unit) - other.value, other.unit)
        }
        comparable < 0 -> {
            MutableKhrono(other.convertTo(this.unit) - this.value, this.unit)
        }
        else -> {
            MutableKhrono(this.value - other.value, this.unit)
        }
    }
}

operator fun MutableKhrono.minusAssign(other: Number) {
    this.apply { this.value -= other.toDouble() }
}

operator fun MutableKhrono.minusAssign(other: Khrono) {
    val comparable = this.unit.compareTo(other.unit)

    when {
        // This context has the greater time unit. Convert down to other.
        comparable > 0 -> {
            this.value = this.convertTo(other.unit) - other.value
            this.unit = other.unit
        }
        comparable < 0 -> {
            this.value = other.convertTo(this.unit) - this.value
        }
        else -> {
            this.value = this.value - other.value
        }
    }
}