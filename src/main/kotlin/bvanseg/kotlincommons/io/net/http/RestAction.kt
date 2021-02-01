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
package bvanseg.kotlincommons.io.net.http

import bvanseg.kotlincommons.time.api.KTime
import bvanseg.kotlincommons.util.any.delay
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.http.HttpResponse
import java.util.concurrent.CompletableFuture

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
    protected var errorCallback: ((HttpResponse<*>) -> Unit)? = null
    protected var exceptionCallback: ((HttpResponse<*>?, Throwable) -> Unit)? = null

    var future: CompletableFuture<out HttpResponse<*>>? = null
        protected set

    open fun onSuccess(callback: (HttpResponse<*>) -> Unit): RestAction<T> {
        successCallback = callback
        return this
    }

    open fun onError(callback: (HttpResponse<*>) -> Unit): RestAction<T> {
        errorCallback = callback
        return this
    }

    open fun onException(callback: (HttpResponse<*>?, Throwable) -> Unit): RestAction<T> {
        exceptionCallback = callback
        return this
    }

    fun queue(): RestAction<T> = queueImpl()
    fun queue(callback: (T) -> Unit): RestAction<T> = queueImpl(callback)

    fun queueAfter(delay: KTime) = GlobalScope.launch {
        delay(delay)
        queueImpl()
    }
    fun queueAfter(delay: KTime, callback: (T) -> Unit) = GlobalScope.launch {
        delay(delay)
        queueImpl(callback)
    }

    fun submit(): CompletableFuture<out HttpResponse<*>>? {
        queueImpl()
        return future
    }
    fun submit(callback: (T) -> Unit): CompletableFuture<out HttpResponse<*>>? {
        queueImpl(callback)
        return future
    }

    fun complete() = completeImpl()

    protected abstract fun queueImpl(): RestAction<T>
    protected abstract fun queueImpl(callback: (T) -> Unit): RestAction<T>
    protected abstract fun completeImpl(): T?

    fun <O> flatMap(callback: (T?) -> RestAction<O>): FlatMapRestAction<T, O> = FlatMapRestAction(callback, this)
}