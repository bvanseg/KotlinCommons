package bvanseg.kotlincommons.tuples

/**
 * A mutable version of the [Pair] class.
 *
 * @author bright_spark
 * @since 2.2.5
 */
data class MutablePair<A, B>(var first: A, var second: B) {
	override fun toString(): String = "($first, $second)"
}