package bvanseg.kcommons.numbers

import java.text.NumberFormat


private val numberFormat = NumberFormat.getNumberInstance()

/**
 * Formats this number to a [String] with digit grouping
 */
fun Number.format(): String = numberFormat.format(this)

fun <T: Number> Number.clamp(min: T, max: T): T = when {
    this.toDouble() < min.toDouble() -> min
    this.toDouble() > max.toDouble() -> max
    else -> (this as T)
}