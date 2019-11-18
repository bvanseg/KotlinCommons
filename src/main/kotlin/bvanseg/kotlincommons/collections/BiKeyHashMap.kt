package bvanseg.kotlincommons.collections

/**
 * A [Collection] class that supports two keys ([K1], [K2]) mapped to a single value [V].
 *
 * @author Boston Vanseghi
 * @since 2.1.6
 */
class BiKeyHashMap<K1, K2, V>: Iterable<Map.Entry<K1, V>> {

    private val backingMap1 = DualHashMap<K1, V>()
    private val backingMap2 = DualHashMap<K2, V>()

    fun contains(key1: K1, key2: K2): Boolean = backingMap1.contains(key1) && backingMap2.contains(key2)
    fun containsValue(value: V): Boolean = backingMap1.containsValue(value) && backingMap2.containsValue(value)
    fun isEmpty(): Boolean = backingMap1.isEmpty() && backingMap2.isEmpty()

    fun put(key1: K1, key2: K2, value: V) {
        backingMap1[key1] = value
        backingMap2[key2] = value
    }

    fun get(key1: K1?, key2: K2?): V? = backingMap1[key1] ?: backingMap2[key2]

    fun getFirst(key1: K1?): V? = backingMap1[key1]
    fun getSecond(key1: K2?): V? = backingMap2[key1]

    fun remove(key1: K1, key2: K2): V? {
        val value1: V? = backingMap1.remove(key1)
        val value2 = backingMap2.remove(key2)
        return value1 ?: value2
    }

    fun clear() {
        backingMap1.clear()
        backingMap2.clear()
    }

    fun reverse() = backingMap1.reverse()
    fun secondReverse() = backingMap2.reverse()

    override fun iterator(): Iterator<Map.Entry<K1, V>> = backingMap1.entries.iterator()
    fun secondIterator(): Iterator<Map.Entry<K2, V>> = backingMap2.entries.iterator()
}