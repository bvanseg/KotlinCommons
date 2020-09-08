package bvanseg.kotlincommons.cache

import java.util.concurrent.ConcurrentHashMap

/**
 * @author Boston Vanseghi
 * @since 2.5.0
 */
class Cache<K, V>(private val defaultTimeToLiveMS: Long) {

    val map = ConcurrentHashMap<K, CacheEntry<V>>()

    fun give(key: K, value: V, timeToLiveMS: Long = defaultTimeToLiveMS) = insert(createKey(key, timeToLiveMS), value)

    private fun insert(key: CacheKey<K>, value: V) {
        map[key.value] = CacheEntry(value, key.timeToLiveMS + System.currentTimeMillis())
    }

    private fun createKey(value: K, timeToLiveMS: Long): CacheKey<K> = CacheKey(value, timeToLiveMS)

    fun clear() = map.clear()
    fun remove(key: K) = map.remove(key)

    fun computeIfAbsent(key: K, timeToLiveMS: Long = defaultTimeToLiveMS, callback: (K) -> V): V = map.compute(key) { k, entry ->
        val currentTimeMS = System.currentTimeMillis()
        if(entry != null && currentTimeMS < entry.expiration)
            entry
        else
            CacheEntry(callback(k), currentTimeMS + timeToLiveMS)
    }!!.value

    override fun toString(): String = map.toString()
}