package bvanseg.kotlincommons.collections

class ReverseMutableMap<V, K>(private val map: MutableMap<K, V>): HashMap<V, K>() {

    override val size: Int
        get() = map.size

    override fun get(key: V): K? = map.entries.find { it.value == key }?.key

    override fun put(key: V, value: K): K? {
        map[value] = key
        return value
    }
}