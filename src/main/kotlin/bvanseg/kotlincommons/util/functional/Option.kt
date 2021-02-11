package bvanseg.kotlincommons.util.functional

/**
 * @author Boston Vanseghi
 * @since 2.9.0
 */
sealed class Option<out T> {

    data class Some<out T>(val value: T): Option<T>() {
        override fun toString(): String = value.toString()
    }

    object None: Option<Nothing>() {
        override fun toString(): String = "None"
    }

    val isSome: Boolean
        get() = this is Some

    val isNone: Boolean
        get() = this is None

    fun unwrap(): T = unwrapOrNull() ?: throw IllegalStateException("Expected type 'Some' but got 'None' instead!")
    fun unwrapOrNull(): T? = if (isSome) (this as Some<T>).value else null

    fun <T> contains(value: T): Boolean = this is Some && this.value == value
}
