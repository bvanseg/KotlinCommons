package bvanseg.kotlincommons.grouping.collection

import java.util.Collections
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
class IndexedConcurrentHashMap<K: Any, V: Any> {

    /**
     * Orders map values by an index.
     */
    private val orderedValues = Collections.synchronizedList(mutableListOf<V>())

    /**
     * Maps a key to its index.
     */
    private val keyToIndex = DualHashMap<K, Int>()

    /**
     * The backing map for this class.
     */
    private val backingMap = ConcurrentHashMap<K, V>()

    val size: Int
        get() = backingMap.size

    val entries: Set<Map.Entry<K, V>>
        get() = backingMap.entries

    val keys: Set<K>
        get() = backingMap.keys

    val values: Collection<V>
        get() = backingMap.values

    operator fun set(key: K, value: V) = synchronized(this) {
        val nextIndex = orderedValues.size
        if (orderedValues.add(value)) {
            backingMap[key] = value
            synchronized(keyToIndex) { keyToIndex[key] = nextIndex }
        }
    }

    operator fun get(key: K): V? = synchronized(this) { backingMap[key] }
    fun put(key: K, value: V) = synchronized(this) { set(key, value) }

    fun compute(key: K, callback: (K, V?) -> V?) = synchronized(this) {
        backingMap.compute(key) { k, value ->
            val result = callback(k, value)
            val nextIndex = orderedValues.size

            if (result != null && orderedValues.add(result)) {
                synchronized(keyToIndex) { keyToIndex[key] = nextIndex }
            }

            result
        }
    }

    fun computeIfAbsent(key: K, callback: (K) -> V) = synchronized(this) {
        backingMap.computeIfAbsent(key) { k ->
            val result = callback(k)
            val nextIndex = orderedValues.size

            if (orderedValues.add(result)) {
                synchronized(keyToIndex) { keyToIndex[key] = nextIndex }
            }

            result
        }
    }

    fun remove(key: K): V? = synchronized(this) { removeAt(keyToIndex[key] ?: -1) }

    fun removeAt(index: Int): V? = synchronized(this) {
        val key = keyToIndex.reverse()[index]
        val oldValue = backingMap.remove(key)

        if (index in 0 until orderedValues.size) {
            orderedValues.removeAt(index)
        }

        keyToIndex.remove(key)

        return oldValue
    }

    fun isEmpty() = backingMap.isEmpty()
    fun isNotEmpty() = backingMap.isNotEmpty()

    fun containsKey(key: K) = backingMap.containsKey(key)
    fun containsValue(value: V) = backingMap.containsValue(value)

    /** LIST MIRRORS **/
    fun getIndexFromValue(value: V) = synchronized(this) { orderedValues.indexOf(value) }
    fun get(index: Int): V? = synchronized(this) { if(index !in 0 until orderedValues.size) orderedValues[index] else null }
    fun first(): V = synchronized(this) { orderedValues.first() }
    fun last(): V = synchronized(this) { orderedValues.last() }
    fun firstOrNull(): V? = synchronized(this) { orderedValues.firstOrNull() }
    fun lastOrNull(): V? = synchronized(this) { orderedValues.lastOrNull() }

    fun removeFirst() = removeAt(0)
    fun removeLast() = removeAt(orderedValues.size - 1)

    /** CUSTOM **/
    fun getIndexFromKey(key: K): Int = synchronized(this) { keyToIndex[key] ?: -1 }
    fun getKeyFor(index: Int): K = synchronized(this) { keyToIndex.reverse()[index]!! }
    fun before(key: K): V? = synchronized(this) {
        val index = keyToIndex[key]

        return index?.let {
            if (it - 1 < 0 || it - 1 >= orderedValues.size) return@let null
            orderedValues[it - 1]
        }
    }
    fun after(key: K): V? = synchronized(this) {
        val index = keyToIndex[key]

        return index?.let {
            if (it + 1 < 0 || it + 1 >= orderedValues.size) return@let null
            orderedValues[it + 1]
        }
    }

    fun clear() = synchronized(this) {
        backingMap.clear()
        keyToIndex.clear()
        orderedValues.clear()
    }
}