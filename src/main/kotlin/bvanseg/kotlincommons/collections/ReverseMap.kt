package bvanseg.kotlincommons.collections

class ReverseMap<V, K>(private val map: Map<K, V>): HashMap<V, K>() {

    override val size: Int
        get() = map.size

    override fun get(key: V): K? = map.entries.find { it.value == key }?.key
}