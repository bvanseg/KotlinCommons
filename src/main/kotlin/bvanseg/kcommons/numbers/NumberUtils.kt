package bvanseg.kcommons.numbers

fun <T: Number> clamp(value: T, min: T, max: T): T  = when {
    value.toDouble() < min.toDouble() -> min
    value.toDouble() > max.toDouble() -> max
    else -> value
}

fun <T: Number> clampOrNull(value: T?, min: T, max: T): T? {
    if(value == null) return null
    return when {
        value.toDouble() < min.toDouble() -> min
        value.toDouble() > max.toDouble() -> max
        else -> value
    }
}