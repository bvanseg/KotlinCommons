package bvanseg.kotlincommons.comparable

/**
 * Clamps the given [value] between a [min] and a [max].
 *
 * @param value The value to be clamped.
 * @param min The minimum that the value can not fall under.
 * @param max The maximum that the value can not go over.
 *
 * @return The [Comparable] value between the minimum or maximum.
 * @since 1.0.1
 */
fun <T : Comparable<T>> clamp(value: T, min: T, max: T): T = when {
    value < min -> min
    value > max -> max
    else -> value
}

/**
 * Clamps the given [value] between a [min] and a [max].
 *
 * @param value The value to be clamped. Can be null.
 * @param min The minimum that the value can not fall under.
 * @param max The maximum that the value can not go over.
 *
 * @return The [Comparable] value between the minimum or maximum or null if the value is null.
 * @since 1.0.1
 */
fun <T : Comparable<T>> clampOrNull(value: T?, min: T, max: T): T? = when {
    value == null -> null
    value < min -> min
    value > max -> max
    else -> value
}