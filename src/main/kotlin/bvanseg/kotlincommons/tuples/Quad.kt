package bvanseg.kotlincommons.tuples

/**
 * Represents a quad of data. Similar to a [Triple].
 *
 * @author Boston Vanseghi
 * @since 2.0.2
 */
data class Quad<out A, out B, out C, out D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
) {

    /**
     * Returns string representation of the [Quad] including its [first], [second], [third], and [fourth] values.
     */
    override fun toString(): String = "($first, $second, $third, $fourth)"
}

/**
 * Converts this quad into a list.
 */
fun <T> Quad<T, T, T, T>.toList(): List<T> = listOf(first, second, third, fourth)