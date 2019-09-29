package kcommons.utilities.numbers

fun <T: Number> clamp(value: T, min: T, max: T): T  = when {
            value.toDouble() < min.toDouble() -> min
            value.toDouble() > max.toDouble() -> max
            else -> value
        }