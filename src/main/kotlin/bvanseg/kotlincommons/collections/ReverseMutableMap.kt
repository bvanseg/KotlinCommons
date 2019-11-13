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
package bvanseg.kotlincommons.collections

/**
 * Represents a reversed [Map], where instead of keys [K] mapped to values [V], it is values mapped to their keys.
 * The contents of this particular [ReverseMap] can be altered.
 *
 * @Boston Vanseghi
 * @since 2.0.1
 */
class ReverseMutableMap<V, K>(private val map: MutableMap<K, V>): HashMap<V, K>() {

    override val size: Int
        get() = map.size

    override fun get(key: V): K? = map.entries.find { it.value == key }?.key

    override fun put(key: V, value: K): K? {
        map[value] = key
        return value
    }
}