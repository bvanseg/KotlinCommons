package bvanseg.kotlincommons.util.functional

/**
 * A customized version of [Either] that has appropriate naming for a failure/success outcome.
 *
 * @author Boston Vanseghi
 * @since 2.9.0
 */
sealed class Result<out F, out S> {

    data class Failure<F>(val value: F) : Result<F, Nothing>() {
        override fun toString(): String = value.toString()
    }

    data class Success<S>(val value: S) : Result<Nothing, S>() {
        override fun toString(): String = value.toString()
    }

    val isFailure: Boolean
        get() = this is Failure<F>

    val isSuccess: Boolean
        get() = this is Success<S>

    fun failure(): Option<F> = if (isFailure) Option.Some((this as Failure<F>).value) else Option.None
    fun success(): Option<S> = if (isSuccess) Option.Some((this as Success<S>).value) else Option.None

    inline fun <T, U> map(crossinline failure: (F) -> T, crossinline success: (S) -> U): Result<T, U> = when {
        isFailure -> Failure(failure((this as Failure<F>).value))
        isSuccess -> Success(success((this as Success<S>).value))
        else -> throw IllegalStateException("Result is neither failure nor success!")
    }

    inline fun <C> mapFailure(initial: C, crossinline failure: (initial: C, value: F) -> C): Failure<C> = Failure(
        when {
            isFailure -> failure(initial, (this as Failure<F>).value)
            isSuccess -> initial
            else -> throw IllegalStateException("Result is neither failure nor success!")
        }
    )

    inline fun <C> mapSuccess(initial: C, crossinline success: (initial: C, value: S) -> C): Success<C> = Success(
        when {
            isFailure -> initial
            isSuccess -> success(initial, (this as Success<S>).value)
            else -> throw IllegalStateException("Result is neither failure nor success!")
        }
    )

    inline fun ifFailure(callback: (F) -> Unit): Result<F, S> {
        if (isFailure) {
            callback(this.failure().unwrap())
        }
        return this
    }

    inline fun ifSuccess(callback: (S) -> Unit): Result<F, S> {
        if (isSuccess) {
            callback(this.success().unwrap())
        }
        return this
    }

    fun flip(): Result<S, F> = when {
        isFailure -> Success((this as Failure<F>).value)
        isSuccess -> Failure((this as Success<S>).value)
        else -> throw IllegalStateException("Result is neither failure nor success!")
    }

    fun failureOrNull() = if (this.isFailure) (this as Failure<F>).value else null
    fun successOrNull() = if (this.isSuccess) (this as Success<S>).value else null
}