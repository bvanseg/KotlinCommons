/*
 * MIT License
 *
 * Copyright (c) 2020 Boston Vanseghi
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
package bvanseg.kotlincommons.net.http

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