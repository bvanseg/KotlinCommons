package bvanseg.kotlincommons.net.rest

import java.net.http.HttpResponse

/**
 * A simple interface that outlines a model of handling RESTful requests.
 *
 * [T] - The type of model to perform the REST action on.
 *
 * @author Boston Vanseghi
 * @since 2.3.0
 */
abstract class RestAction<T> {
    protected var successCallback: ((HttpResponse<*>) -> Unit)? = null
    protected var exceptionCallback: ((Throwable) -> Unit)? = null

    open fun onSuccess(callback: (HttpResponse<*>) -> Unit): RestAction<T> {
        successCallback = callback
        return this
    }

    open fun onException(callback: (Throwable) -> Unit): RestAction<T> {
        exceptionCallback = callback
        return this
    }

    open fun queue() = queueImpl()
    open fun queue(callback: (T) -> Unit) = queueImpl(callback)
    open fun complete() = completeImpl()

    /**
     * Intended to send a REST request asynchronously.
     */
    protected abstract fun queueImpl()

    /**
     * Has the same intention as [queue], but instead takes a callback to execute upon the async request completing.
     */
    protected abstract fun queueImpl(callback: (T) -> Unit)

    /**
     * Intended to fully block until the model is returned.
     */
    protected abstract fun completeImpl(): T

    fun <O> flatMap(callback: (T?) -> RestAction<O>): FlatMapRestAction<T, O> = FlatMapRestAction(callback, this)
}