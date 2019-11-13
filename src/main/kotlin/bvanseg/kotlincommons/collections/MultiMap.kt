package bvanseg.kotlincommons.collections

import java.util.*

abstract class MultiMap<K, out V> {

    protected abstract val backingMap: Map<K, Collection<V>>

    abstract val isEmpty: Boolean

    abstract fun keySet(): Set<K>

    abstract fun entrySet(): Set<Map.Entry<K, Collection<V>>>

    abstract fun values(): Collection<Collection<V>>

    abstract fun containsKey(key: K): Boolean

    abstract fun size(): Int
}