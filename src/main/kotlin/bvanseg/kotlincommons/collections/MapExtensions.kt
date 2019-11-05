package bvanseg.kotlincommons.collections

/**
 * Reverses the [Map], swapping keys with values and values with keys.
 *
 * @return A [Map] view with type args <V, K> instead of <K, V>.
 *
 * @author Boston Vanseghi
 * @since 2.0.1
 */
fun <K, V> Map<K, V>.reversed(): Map<V, K> = ReverseMap(this)

/**
 * Reverses the [MutableMap], swapping keys with values and values with keys.
 *
 * @return A [MutableMap] view of the given [MutableMap] with type args <V, K> instead of <K, V>.
 *
 * @author Boston Vanseghi
 * @since 2.0.1
 */
fun <K, V> MutableMap<K, V>.reversedMutable(): MutableMap<V, K> = ReverseMutableMap(this)