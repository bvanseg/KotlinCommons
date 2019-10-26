package bvanseg.kotlincommons.comparable

/**
 * Clamps the [Comparable] object between a [min] and a [max].
 *
 * @param min The minimum that the value can not fall under.
 * @param max The maximum that the value can not go over.
 *
 * @return The [Comparable] value between the minimum or maximum.
 * @since 1.0.1
 */
fun <T : Comparable<T>> T.clamp(min: T, max: T): T = when {
    this < min -> min
    this > max -> max
    else -> this
}

/**
 * Clamps the [Comparable] object between a [min] and a [max].
 *
 * @param min The minimum that the value can not fall under.
 * @param max The maximum that the value can not go over.
 *
 * @return The [Comparable] value between the minimum or maximum or null if the value is null.
 * @since 1.0.1
 */
fun <T : Comparable<T>> T?.clampOrNull(min: T, max: T): T? = when {
    this == null -> null
    this < min -> min
    this > max -> max
    else -> this
}