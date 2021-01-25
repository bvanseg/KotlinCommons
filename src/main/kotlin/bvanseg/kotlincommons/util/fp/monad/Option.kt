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
package bvanseg.kotlincommons.util.fp.monad

/**
 * ```
 * val myInt = 1.some()
 * val myString = "hello, world".some()
 * val myNewInt = myInt.map{
 *  it + 1
 * }
 * val myNewString = myString.map{
 *  it += "!"
 * }
 * ```
 */

/**
 * @author Alex Couch
 * @since 2.4.0
 */
sealed class Option<T> {
    data class Some<T>(val t: T) : Option<T>()
    class None<T> : Option<T>() {
        companion object
    }

    val isSome get() = this is Some<T>
    val isNone get() = this is None
    val unwrap get() = if (this is Some<T>) t else throw IllegalStateException("Attempted to unwrap Option but was None")

    fun unwrapOrElse(orElse: () -> T): T =
        if (this is Some<T>) t else orElse()

    fun <S> map(callback: (T) -> S): Option<S> =
        if (this is Some<T>) {
            callback(t).some()
        } else {
            none()
        }

    @Suppress("UNCHECKED_CAST")
    internal fun <S> fix(): Option<S> = this as Option<S>

    fun <S> flatMap(callback: (T) -> Option<S>): Option<S> =
        when (this) {
            is Some<T> -> {
                callback(t)
            }
            else -> none()
        }
}

fun <T> T.some() = Option.Some(this)
fun <T> none() = Option.None<T>()