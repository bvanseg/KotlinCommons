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

/**
 * Takes a [HashMap] and creates a [DualHashMap] with it, allowing it to be reversed.
 *
 * @author Boston Vanseghi
 * @since 2.1.2
 */
fun <K, V> HashMap<K, V>.toDualMap(): DualHashMap<K, V> = DualHashMap(this)

/**
 * @author Boston Vanseghi
 * @since 2.11.0
 */
fun <K, V> Map<K, V>.joinToString(separator: String = ", ", callback: (Map.Entry<K, V>) -> CharSequence): String {
    val builder = StringBuilder()
    var count = 0
    for (element in this) {
        if (++count > 1) builder.append(separator)
        builder.append(callback(element))
    }
    return builder.toString()
}