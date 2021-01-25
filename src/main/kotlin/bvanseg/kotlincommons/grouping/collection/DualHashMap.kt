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
package bvanseg.kotlincommons.grouping.collection

import java.util.HashMap

/**
 * A bi-directional [HashMap] that can be reversed using [reverse].
 *
 * @author Boston Vanseghi
 * @since 2.1.2
 */
class DualHashMap<K, V> : MutableMap<K, V> {

    private val forward: HashMap<K, V>
    private val backward: HashMap<V, K>

    constructor() : this(hashMapOf<K, V>(), hashMapOf<V, K>())

    constructor(forward: HashMap<K, V>) : this(forward, hashMapOf()) {
        backward.entries.forEach { backward[it.key] = it.value }
    }

    constructor(forward: HashMap<K, V>, backward: HashMap<V, K>) {
        this.forward = forward
        this.backward = backward
    }

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = forward.entries

    override val keys: MutableSet<K>
        get() = forward.keys

    override val values: MutableCollection<V>
        get() = forward.values

    override val size: Int
        get() = forward.size

    override fun put(key: K, value: V): V? = forward.put(key, value).also {
        backward[value] = key
        return it
    }

    override fun remove(key: K): V? = forward.remove(key)?.let {
        backward.remove(it)
        return it
    }

    override fun containsKey(key: K): Boolean = forward.containsKey(key)

    override fun containsValue(value: V): Boolean = backward.containsKey(value)

    override fun get(key: K): V? = forward[key]

    override fun isEmpty(): Boolean = forward.isEmpty() && backward.isEmpty()

    override fun clear() {
        forward.clear()
        backward.clear()
    }

    override fun putAll(from: Map<out K, V>) = entries.forEach {
        put(it.key, it.value)
    }

    fun reverse(): DualHashMap<V, K> = DualHashMap(backward, forward)
}