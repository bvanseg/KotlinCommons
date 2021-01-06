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
package bvanseg.kotlincommons.enum

import java.util.EnumSet

/**
 *
 * @author Boston Vanseghi
 * @since 2.5.0
 */
inline fun <reified E : Enum<E>> enumSetOf(vararg values: E): EnumSet<E> = when {
    values.isEmpty() -> EnumSet.noneOf(E::class.java)
    values.size == 1 -> EnumSet.of(values.first())
    else -> EnumSet.of(values.first(), *values.slice(1 until values.size).toTypedArray())
}

/**
 * Returns an enum entry with the specified name or `null` if no such entry was found.
 */
inline fun <reified T : Enum<T>> enumValueOfOrNull(name: String?, ignoreCase: Boolean = false): T? =
    if (name == null) null else enumValues<T>().find { it.name.equals(name, ignoreCase) }
