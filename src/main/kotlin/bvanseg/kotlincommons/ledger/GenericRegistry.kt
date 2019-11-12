package bvanseg.kotlincommons.ledger

/**
 * @author bright_spark
 * @since 2.1.0
 */
open class GenericRegistry<K : Any, V : Any>(private val keyExtractor: ((V) -> K)? = null): Iterable<V> {

    protected open val entries = mutableMapOf<K, V>()

    open fun add(
        value: V,
        key: K = keyExtractor?.invoke(value) ?: throw RuntimeException("No key provided for new value $value")
    ): Boolean {
        if (entries.containsKey(key))
            return false
        entries[key] = value
        return true
    }

    open fun get(key: K): V? = entries[key]

    override fun iterator(): Iterator<V> = entries.values.iterator()
}