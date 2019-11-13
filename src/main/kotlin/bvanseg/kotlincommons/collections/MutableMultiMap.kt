package bvanseg.kotlincommons.collections

import java.util.*

class MutableMultiMap<K, V>: MultiMap<K, V>() {

    override val backingMap: HashMap<K, MutableCollection<V>> = HashMap()

    override val isEmpty: Boolean
        get() = backingMap.isEmpty()

    fun put(key: K, value: V) {
        if (backingMap[key] == null)
            backingMap[key] = ArrayList()

        backingMap[key]!!.add(value)
    }

    fun putIfAbsent(key: K, value: V) {
        if (backingMap[key] == null)
            backingMap[key] = ArrayList()

        if (!backingMap[key]!!.contains(value))
            backingMap[key]!!.add(value)
    }

    operator fun get(key: K): MutableCollection<V> = backingMap[key]!!

    override fun keySet(): Set<K> = backingMap.keys

    override fun entrySet(): Set<Map.Entry<K, Collection<V>>> = backingMap.entries

    override fun values(): Collection<MutableCollection<V>> = backingMap.values

    override fun containsKey(key: K): Boolean = backingMap.containsKey(key)

    fun remove(key: K): MutableCollection<V>? = backingMap.remove(key)

    override fun size(): Int = backingMap.values.sumBy { it.size }

    fun clear() = backingMap.clear()

    fun remove(key: K, value: V): Boolean = if (backingMap[key] != null) backingMap[key]?.remove(value) ?: false else false

    fun replace(key: K, oldValue: V, newValue: V): Boolean {
        if (backingMap[key] != null) {
            if (backingMap[key]!!.remove(oldValue))
                return backingMap[key]!!.add(newValue)
        }
        return false
    }
}