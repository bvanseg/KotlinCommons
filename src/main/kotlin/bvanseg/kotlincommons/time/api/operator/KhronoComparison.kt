package bvanseg.kotlincommons.time.api.operator

import bvanseg.kotlincommons.time.api.Khrono

operator fun Number.compareTo(other: Khrono): Int {
    val thisValue = this.toDouble()
    return when {
        thisValue == other.value -> 0
        thisValue > other.value -> 1
        thisValue < other.value -> -1
        else -> -1
    }
}