package bvanseg.kotlincommons.collections

/**
 * Reverses the [Map], swapping keys with values and values with keys.
 *
 * @return A new [Map] with type args <V, K> instead of <K, V>.
 * @since 2.0.1
 */
fun <K, V> Map<K, V>.reverse(): Map<V, K> = mutableMapOf<V, K>().also {
    this.forEach { (k, v) -> it[v] = k }
}