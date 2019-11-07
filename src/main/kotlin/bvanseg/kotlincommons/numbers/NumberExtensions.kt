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
 * @return True if the [Number] is even.
 *
 * @author Boston Vanseghi
 * @since 2.0.1
 */
fun Number.isEven(): Boolean = this.toDouble() % 2 == 0.0

/**
 * Checks whether the number is odd or not.
 *
 * @return True if the [Number] is odd.
 *
 * @author bright_spark
 */
fun Number.isOdd(): Boolean = !isEven()

/**
 * Returns the value of this [Number] as the number type [T]
 *
 * @return The value of this number as [T]
 *
 * @author bright_spark
 */
inline fun <reified T : Number> Number.to(): T = when (T::class) {
    Byte::class -> this.toByte() as T
    Short::class -> this.toShort() as T
    Int::class -> this.toInt() as T
    Long::class -> this.toLong() as T
    Float::class -> this.toFloat() as T
    Double::class -> this.toDouble() as T
    else -> throw RuntimeException("The destination type ${T::class} is not a primitive!")
}

/**
 * Will execute [block] if this number [T] is positive.
 *
 * @return This number
 *
 * @author bright_spark
 */
inline fun <reified T> T.ifPositive(block: (T) -> Unit): T where T : Number, T : Comparable<T> {
    if (this > 0.to())
        block(this)
    return this
}

/**
 * Will execute [block] if this number [T] is negative.
 *
 * @return This number
 *
 * @author bright_spark
 */
inline fun <reified T> T.ifNegative(block: (T) -> Unit): T where T : Number, T : Comparable<T> {
    if (this < 0.to())
        block(this)
    return this
}
