package bvanseg.kcommons.numbers

fun <T: Number> Number.clamp(min: T, max: T): T = when {
    this.toDouble() < min.toDouble() -> min
    this.toDouble() > max.toDouble() -> max
    else -> (this as T)
}