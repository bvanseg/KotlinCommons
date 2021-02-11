package bvanseg.kotlincommons.util.functional

/**
 * @author Boston Vanseghi
 * @since 2.9.0
 */
sealed class Either<out L, out R> {

    data class Left<L>(val value: L) : Either<L, Nothing>() {
        override fun toString(): String = value.toString()
    }

    data class Right<R>(val value: R) : Either<Nothing, R>() {
        override fun toString(): String = value.toString()
    }

    val isLeft: Boolean
        get() = this is Left<L>

    val isRight: Boolean
        get() = this is Right<R>

    fun left(): Option<L> = if (isLeft) Option.Some((this as Left<L>).value) else Option.None
    fun right(): Option<R> = if (isRight) Option.Some((this as Right<R>).value) else Option.None

    fun <T, U> map(left: (L) -> T, right: (R) -> U): Either<T, U> = when {
        isLeft -> Left(left((this as Left<L>).value))
        isRight -> Right(right((this as Right<R>).value))
        else -> throw IllegalStateException("Either is neither left nor right!")
    }

    fun <C> mapLeft(initial: C, left: (initial: C, value: L) -> C): Left<C> = Left(
        when {
            isLeft -> left(initial, (this as Left<L>).value)
            isRight -> initial
            else -> throw IllegalStateException("Either is neither left nor right!")
        }
    )

    fun <C> mapRight(initial: C, right: (initial: C, value: R) -> C): Right<C> = Right(
        when {
            isLeft -> initial
            isRight -> right(initial, (this as Right<R>).value)
            else -> throw IllegalStateException("Either is neither left nor right!")
        }
    )

    fun flip(): Either<R, L> = when {
        isLeft -> Right((this as Left<L>).value)
        isRight -> Left((this as Right<R>).value)
        else -> throw IllegalStateException("Either is neither left nor right!")
    }

    fun leftOrNull() = if (this.isLeft) (this as Left<L>).value else null
    fun rightOrNull() = if (this.isRight) (this as Right<R>).value else null
}