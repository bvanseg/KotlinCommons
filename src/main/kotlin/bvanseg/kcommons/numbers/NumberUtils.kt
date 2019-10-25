package bvanseg.kcommons.numbers

fun <T : Comparable<T>> clamp(value: T, min: T, max: T): T = when {
    value < min -> min
    value > max -> max
    else -> value
}

fun <T : Comparable<T>> clampOrNull(value: T?, min: T, max: T): T? = when {
    value == null -> null
    value < min -> min
    value > max -> max
    else -> value
}