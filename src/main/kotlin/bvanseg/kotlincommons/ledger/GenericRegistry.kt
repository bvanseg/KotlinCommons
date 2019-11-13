/*
 * MIT License
 *
 * Copyright (c) 2019 Boston Vanseghi
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
package bvanseg.kotlincommons.ledger

/**
 * A generic registry class that can map a key to a value. Also provides some additional utilities, such as key extracting.
 *
 * @author bright_spark
 * @since 2.1.0
 */
open class GenericRegistry<K : Any, V : Any>(private val keyExtractor: ((V) -> K)? = null): Iterable<V> {

    protected open val entries = mutableMapOf<K, V>()

    open fun add(
        value: V,
        key: K = keyExtractor?.invoke(value) ?: throw RuntimeException("No key provided for new value $value")
    ): Boolean {
        if (entries.containsKey(key))
            return false
        entries[key] = value
        return true
    }

    open fun get(key: K): V? = entries[key]

    override fun iterator(): Iterator<V> = entries.values.iterator()
}