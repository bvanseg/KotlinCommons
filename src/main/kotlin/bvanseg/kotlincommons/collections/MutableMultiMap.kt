/*
 * MIT License
 *
 * Copyright (c) 2020 Boston Vanseghi
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
package bvanseg.kotlincommons.collections

import java.util.*

/**
 * A class used in mapping multiple values [V] to a single key [K]. The values of this particular [MultiMap] can be altered.
 *
 * @author Boston Vanseghi
 * @since 2.1.1
 */
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