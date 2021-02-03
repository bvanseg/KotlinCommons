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
package bvanseg.kotlincommons.lang.thread

import bvanseg.kotlincommons.KotlinCommons
import java.util.concurrent.ConcurrentHashMap

private fun validateThreadAttributeMap() {
    val currentThreadID = Thread.currentThread().id

    if (KotlinCommons.KC_THREAD_ATTRIBUTE_MAP[currentThreadID] == null) {
        KotlinCommons.KC_THREAD_ATTRIBUTE_MAP[currentThreadID] = ConcurrentHashMap()
    }
}

fun <T> Thread.getAttribute(name: String): T? {
    validateThreadAttributeMap()
    val currentThreadID = this.id

    @Suppress("UNCHECKED_CAST")
    val value = KotlinCommons.KC_THREAD_ATTRIBUTE_MAP[currentThreadID]!![name]?.get() as T?
    return value
}

fun Thread.removeAttribute(name: String) = KotlinCommons.KC_THREAD_ATTRIBUTE_MAP[this.id]?.let {
    it[name]?.remove()
    it.remove(name)
}

fun <T> Thread.setAttribute(name: String, value: T): T {
    validateThreadAttributeMap()
    val currentThreadID = this.id

    if (KotlinCommons.KC_THREAD_ATTRIBUTE_MAP[currentThreadID]!![name] == null) {
        KotlinCommons.KC_THREAD_ATTRIBUTE_MAP[currentThreadID]!![name] = ThreadLocal<T>()
    }

    @Suppress("UNCHECKED_CAST") val attribute =
        KotlinCommons.KC_THREAD_ATTRIBUTE_MAP[currentThreadID]!![name]!! as ThreadLocal<T>

    attribute.set(value)

    return value
}

fun Thread.clearAttributes() = KotlinCommons.KC_THREAD_ATTRIBUTE_MAP.remove(this.id)