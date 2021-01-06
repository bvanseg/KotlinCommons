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

package bvanseg.kotlincommons.fp.monad

import bvanseg.kotlincommons.fp.Kind2

/**
 * @author Alex Couch
 * @since 2.4.0
 */
class ForEitherOf
typealias EitherOf<A, B> = Kind2<ForEitherOf, A, B>

@Suppress("UNCHECKED_CAST")
fun <A, B> EitherOf<A, B>.fix() = this as Either<A, B>

/**
 * val result = someFunction()
 * return result.flatMap{
 *  "An error occurred while doing something: $it"
 * }
 */
sealed class Either<out L, out R> : EitherOf<L, R> {
    data class Left<out L>(val value: L) : Either<L, Nothing>()
    class Right<out R>(val value: R) : Either<Nothing, R>()

    val isLeft get() = this is Left<L>
    val isRight get() = this is Right<R>

    fun <T, U> fold(left: (L) -> T, right: (R) -> U): Either<T, U> =
        fix().let {
            when (this) {
                is Left -> left(value).left()
                is Right -> right(value).right()
            }
        }

    fun <C> foldLeft(initial: C, rightOperation: (C, R) -> C): C =
        fix().let {
            when (it) {
                is Right -> rightOperation(initial, it.value)
                is Left -> initial
            }
        }

    fun <C> foldRight(initial: C, rightOperation: (R, C) -> C): C =
        fix().let {
            when (it) {
                is Right -> rightOperation(it.value, initial)
                is Left -> initial
            }
        }

    inline fun <T> mapLeft(crossinline callback: (L) -> T) =
        fold({ callback(it) }, { this })

    inline fun <T> mapRight(crossinline callback: (R) -> T) =
        fold({ this }, { callback(it) })
}

fun <T> T.left() = Either.Left(this)
fun <T> T.right() = Either.Right(this)

fun <L, R, T> Either<L, R>.flatMap(callback: (L) -> Either<T, R>): Either<T, R> =
    fix().let {
        when (this) {
            is Either.Right -> this
            is Either.Left -> callback(value)
        }
    }