package bvanseg.kotlincommons.fp.dataclasses

import bvanseg.kotlincommons.fp.Kind2

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
sealed class Either<out L, out R>: EitherOf<L, R> {
    data class Left<out L>(val value: L) : Either<L, Nothing>()
    class Right<out R>(val value: R) : Either<Nothing, R>()

    val isLeft get() = this is Left<L>
    val isRight get() = this is Right<R>

    fun <T, U> fold(left: (L) -> T, right: (R) -> U): Either<T, U> =
            fix().let{
                when (this) {
                    is Left -> left(value).left()
                    is Right -> right(value).right()
                }
            }

    fun <C> foldLeft(initial: C, rightOperation: (C, R) -> C): C =
            fix().let {
                when(it){
                    is Right -> rightOperation(initial, it.value)
                    is Left -> initial
                }
            }

    fun <C> foldRight(initial: C, rightOperation: (R, C) -> C): C =
            fix().let {
                when(it){
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
            when(this){
                is Either.Right -> this
                is Either.Left -> callback(value)
            }
        }