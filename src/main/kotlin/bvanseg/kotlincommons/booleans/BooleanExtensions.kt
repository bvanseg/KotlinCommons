package bvanseg.kotlincommons.booleans

/**
 * Will execute [block] if this [Boolean] is true.
 *
 * @return This boolean
 *
 * @author bright_spark
 * @since 2.0.1
 */
inline fun Boolean.ifTrue(block: () -> Unit): Boolean {
    if (this) block()
    return this
}

/**
 * Will execute [block] if this [Boolean] is false.
 *
 * @return This boolean
 *
 * @author bright_spark
 * @since 2.0.1
 */
inline fun Boolean.ifFalse(block: () -> Unit): Boolean {
    if (!this) block()
    return this
}