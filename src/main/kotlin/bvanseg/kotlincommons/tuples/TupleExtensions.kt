package bvanseg.kotlincommons.tuples

/**
 * Checks if the [value] is between [Pair.first] and [Pair.second] (exclusive)
 *
 * @author bright_spark
 * @since 2.0.1
 */
fun <T : Comparable<T>> Pair<T, T>.between(value: T): Boolean = value > first && value < second

/**
 * Checks if the [value] is between [Pair.first] and [Pair.second] (inclusive)
 *
 * @author bright_spark
 * @since 2.0.1
 */
fun <T : Comparable<T>> Pair<T, T>.contains(value: T): Boolean = value in first..second