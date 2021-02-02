package bvanseg.kotlincommons.time.api.operator

import bvanseg.kotlincommons.time.api.Khrono

operator fun Khrono.compareTo(other: Number): Int {
    val otherValue = other.toDouble()
    return when {
        this.value == otherValue -> 0
        this.value > otherValue -> 1
        this.value < otherValue -> -1
        else -> -1
    }
}

operator fun Number.compareTo(other: Khrono): Int {
    val thisValue = this.toDouble()
    return when {
        thisValue == other.value -> 0
        thisValue > other.value -> 1
        thisValue < other.value -> -1
        else -> -1
    }
}

operator fun Khrono.compareTo(other: Khrono): Int {
    val equalUnitValue = this.convertTo(other.unit)
    return when {
        equalUnitValue == other.value -> 0
        equalUnitValue > other.value -> 1
        equalUnitValue < other.value -> -1
        else -> -1
    }
}