package bvanseg.kcommons.numbers

import java.text.NumberFormat


private val numberFormat = NumberFormat.getNumberInstance()

/**
 * Formats this number to a [String] with digit grouping
 */
fun Number.format(): String = numberFormat.format(this)

fun <T : Comparable<T>> T.clamp(min: T, max: T): T = when {
    this < min -> min
    this > max -> max
    else -> this
}

fun <T : Comparable<T>> T?.clampOrNull(min: T, max: T): T? = when {
    this == null -> null
    this < min -> min
    this > max -> max
    else -> this
}