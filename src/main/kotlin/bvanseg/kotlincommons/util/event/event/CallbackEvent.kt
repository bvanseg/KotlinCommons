package bvanseg.kotlincommons.util.event.event

/**
 * Acts as a wrapper around a single-parameter callback, allowing for type erasure in collections.
 *
 * @author Boston Vanseghi
 * @since 2.7.0
 */
class CallbackEvent<T: Any>(val callback: (T) -> Unit) {
    fun invoke(value: T) = callback(value)
}