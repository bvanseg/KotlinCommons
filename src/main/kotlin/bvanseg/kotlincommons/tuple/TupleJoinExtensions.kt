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
package bvanseg.kotlincommons.tuple

/**
 * Allows the concatenation of a [Pair] to an object to produce a [Triple].
 *
 * @author Boston Vanseghi
 * @since 2.7.0
 */
infix fun <A, B, C> Pair<A, B>.to(that: C): Triple<A, B, C> = Triple(this.first, this.second, that)

/**
 * Allows the concatenation of a [Pair] to another [Pair] to produce a [Quad].
 *
 * @author Boston Vanseghi
 * @since 2.7.0
 */
infix fun <A, B, C, D> Pair<A, B>.to(that: Pair<C, D>): Quad<A, B, C, D> =
    Quad(this.first, this.second, that.first, that.second)

/**
 * Allows the concatenation of a [Triple] to an object to produce a [Quad].
 *
 * @author Boston Vanseghi
 * @since 2.7.0
 */
infix fun <A, B, C, D> Triple<A, B, C>.to(that: D): Quad<A, B, C, D> = Quad(this.first, this.second, this.third, that)