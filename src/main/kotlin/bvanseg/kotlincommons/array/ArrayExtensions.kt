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
package bvanseg.kotlincommons.array

import java.util.Random

/**
 * Gets a random element from the [Array].
 *
 * @author Boston Vanseghi
 * @since 2.0.2
 */
fun <T> Array<T>.random(rnd: Random = Random()): T = get(rnd.nextInt(size))

/**
 * Gets a random element from the 2D [Array].
 *
 * @author Boston Vanseghi
 * @since 2.0.2
 */
fun <T> Array<Array<T>>.random(rnd: Random = Random()): T = get(rnd.nextInt(size))[rnd.nextInt(size)]

/**
 * Gets a random element from the 3D [Array].
 *
 * @author Boston Vanseghi
 * @since 2.0.2
 */
fun <T> Array<Array<Array<T>>>.random(rnd: Random = Random()): T =
    get(rnd.nextInt(size))[rnd.nextInt(size)][rnd.nextInt(size)]

/**
 * Checks the [Array] to see if any of the elements are null.
 *
 * @author Boston Vanseghi
 * @since 2.0.2
 */
inline fun <reified T> Array<T>.anyNull(): Boolean = any { it == null }

/**
 * Checks the 2D [Array] to see if any of the elements are null.
 *
 * @author Boston Vanseghi
 * @since 2.0.2
 */
inline fun <reified T> Array<Array<T>>.anyNull(): Boolean = any { e1 -> e1.any { e2 -> e2 == null } }

/**
 * Checks the 3D [Array] to see if any of the elements are null.
 *
 * @author Boston Vanseghi
 * @since 2.0.2
 */
inline fun <reified T> Array<Array<Array<T>>>.anyNull(): Boolean =
    any { e1 -> e1.any { e2 -> e2.any { e3 -> e3 == null } } }