package bvanseg.kotlincommons.numbers

import java.text.NumberFormat


private val numberFormat = NumberFormat.getNumberInstance()

/**
 * Formats this number to a [String] with digit grouping.
 */
fun Number.format(): String = numberFormat.format(this)

/**
 * Checks whether the number is even or not.
 *
 * @return True or false depending on if the [Number] is even or not.
 *
 * @author Boston Vanseghi
 * @since 2.0.1
 */
fun Number.isEven(): Boolean = this.toDouble() % 2 == 0.0