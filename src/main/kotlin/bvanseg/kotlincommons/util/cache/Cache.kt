/*
 * MIT License
 *
 * Copyright (c) 2021 Boston Vanseghi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package bvanseg.kotlincommons.util.cache

import bvanseg.kotlincommons.grouping.collection.IndexedConcurrentHashMap
import bvanseg.kotlincommons.util.project.Experimental
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Boston Vanseghi
 * @since 2.5.0
 */
@Experimental
class Cache<K: Any, V>(private val defaultTimeToLiveMS: Long) {

    private val map = IndexedConcurrentHashMap<K, CacheEntry<V>>()

    private val size
        get() = map.size

    fun isEmpty() = map.isEmpty()
    fun isNotEmpty() = map.isNotEmpty()

    fun give(key: K, value: V, timeToLiveMS: Long = defaultTimeToLiveMS) = insert(createKey(key, timeToLiveMS), value)

    fun getEntry(key: K): CacheEntry<V>? = map[key]

    fun get(key: K): V? {
        val currentTimeMS = System.currentTimeMillis()

        val entry = map[key]

        updateCache()

        if (entry != null && currentTimeMS >= entry.expiration) {
            map.remove(key)
            return null
        }

        return entry?.value
    }

    fun getAndInvalidate(key: K): V? = get(key)?.also { map.remove(key) }

    private fun insert(key: CacheKey<K>, value: V) {
        updateCache()
        map[key.value] = CacheEntry(value, key.timeToLiveMS + System.currentTimeMillis())
    }

    private fun createKey(value: K, timeToLiveMS: Long): CacheKey<K> = CacheKey(value, timeToLiveMS)

    fun clear() = map.clear()
    fun remove(key: K) = map.remove(key)

    fun computeIfAbsent(key: K, timeToLiveMS: Long = defaultTimeToLiveMS, callback: (K) -> V): V =
        map.compute(key) { k, entry ->
            val currentTimeMS = System.currentTimeMillis()
            if (entry != null && currentTimeMS < entry.expiration)
                entry
            else
                CacheEntry(callback(k), currentTimeMS + timeToLiveMS)
        }!!.value

    override fun toString(): String = map.toString()

    fun updateCache() {
        val currentTimeMS = System.currentTimeMillis()
        var first = map.firstOrNull()

        while(first != null) {
            if (currentTimeMS >= first.expiration) {
                map.removeFirst()
                first = map.firstOrNull()
            } else {
                break
            }
        }
    }
}