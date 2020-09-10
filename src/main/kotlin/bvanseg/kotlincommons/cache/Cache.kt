package bvanseg.kotlincommons.cache

import java.util.concurrent.ConcurrentHashMap

/**
 * @author Boston Vanseghi
 * @since 2.5.0
 */
class Cache<K, V>(private val defaultTimeToLiveMS: Long) {

    private val map = ConcurrentHashMap<K, CacheEntry<V>>()

    private val size
        get() = map.size

    fun isEmpty() = map.isEmpty()
    fun isNotEmpty() = map.isNotEmpty()

    fun give(key: K, value: V, timeToLiveMS: Long = defaultTimeToLiveMS) = insert(createKey(key, timeToLiveMS), value)

    fun getEntry(key: K): CacheEntry<V>? = synchronized(map) { map[key] }

    fun get(key: K): V? = synchronized(map) {
        val cacheEntry = map[key]

        val currentTimeMS = System.currentTimeMillis()
        cacheEntry?.let {
            return if(currentTimeMS > it.expiration) {
                null
            } else {
                it.value
            }
        }

        return null
    }

    fun getAndInvalidate(key: K): V? = synchronized(map) {
        val value = getEntry(key)
        map.remove(key)
        return value?.value
    }

    private fun insert(key: CacheKey<K>, value: V) = synchronized(map) {
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