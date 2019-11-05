package bvanseg.kotlincommons.primitives

/**
 * Will execute [block] if this [Boolean] is true
 *
 * @author bright_spark
 * @since 2.0.1
 */
inline fun Boolean.ifTrue(block: () -> Unit): Boolean {
    if (this) block()
    return this
}

/**
 * Will execute [block] if this [Boolean] is false
 *
 * @author bright_spark
 * @since 2.0.1
 */
inline fun Boolean.ifFalse(block: () -> Unit): Boolean {
    if (!this) block()
    return this
}