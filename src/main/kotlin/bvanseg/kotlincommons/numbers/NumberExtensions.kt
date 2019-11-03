package bvanseg.kotlincommons.numbers

import java.text.NumberFormat


private val numberFormat = NumberFormat.getNumberInstance()

/**
 * Formats this number to a [String] with digit grouping.
 */
fun Number.format(): String = numberFormat.format(this)