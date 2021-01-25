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
package bvanseg.kotlincommons.grouping.enum

import java.util.EnumSet

/**
 * Creates an [EnumSet] of the given [Enum] type [E].
 *
 * @param values The values to create an [EnumSet] out of.
 *
 * @return An [EnumSet] with the given [values].
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
 * Gets an [Enum] value of type [T] based on the enum value's [name].
 *
 * @param name The name of the [Enum] value to find.
 * @param ignoreCase Whether or not casing should be ignored when searching for the [Enum] value by name.
 *
 * @Return An [Enum] value with the specified name or null if no such entry was found.
 */
inline fun <reified T : Enum<T>> enumValueOfOrNull(name: String?, ignoreCase: Boolean = false): T? =
    if (name == null) null else enumValues<T>().find { it.name.equals(name, ignoreCase) }
