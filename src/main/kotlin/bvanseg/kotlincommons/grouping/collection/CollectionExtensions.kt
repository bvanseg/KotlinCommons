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
 * Checks if the given [Collection] object shares any elements with the other [collection] object.
 *
 * @return True if [this] contains any elements from within the other [collection], false otherwise.
 *
 * @author Boston Vanseghi
 */
fun <E> Collection<E>.containsAny(collection: Collection<E>): Boolean {
    collection.forEach {
        if (this.contains(it)) {
            return true
        }
    }
    return false
}

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
fun <E> LinkedHashSet<E>.removeFirst() = this.remove(this.firstOrNull())

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
fun <E> LinkedHashSet<E>.removeLast() = this.remove(this.lastOrNull())