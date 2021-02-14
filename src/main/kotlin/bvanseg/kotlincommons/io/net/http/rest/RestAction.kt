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
package bvanseg.kotlincommons.io.net.http.rest

import bvanseg.kotlincommons.io.logging.getLogger
import bvanseg.kotlincommons.time.api.Khrono
import bvanseg.kotlincommons.util.any.delay
import bvanseg.kotlincommons.util.functional.Result
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.http.HttpResponse
import java.util.concurrent.CompletableFuture

/**
 * A simple interface that outlines a model of handling RESTful requests.
 *
 * [F] - The type to use in representing errors/exceptions/failures.
 * [S] - The type to transform the returned HttpResponse body into.
 *
 * @author Boston Vanseghi
 * @since 2.3.0
 */
abstract class RestAction<F, S> {

    companion object {
        private val logger = getLogger()
    }

    protected var successCallback: ((HttpResponse<*>) -> Unit)? = null

    var future: CompletableFuture<out HttpResponse<*>>? = null
        protected set

    open fun onSuccess(callback: (HttpResponse<*>) -> Unit): RestAction<F, S> {
        successCallback = callback
        return this
    }

    fun queue(callback: (Result<F, S>) -> Unit = {}): RestAction<F, S> = queueImpl(callback)

    fun queueAfter(delay: Khrono, callback: (Result<F, S>) -> Unit = {}) = GlobalScope.launch {
        delay(delay)
        queueImpl(callback)
    }

    fun submit(callback: (Result<F, S>) -> Unit = {}): CompletableFuture<out HttpResponse<*>>? = queueImpl(callback).future

    fun complete(): Result<F, S> = completeImpl()

    protected abstract fun queueImpl(callback: (Result<F, S>) -> Unit = {}): RestAction<F, S>
    protected abstract fun completeImpl(): Result<F, S>

    fun <O> flatMap(callback: (Result<F, S>) -> RestAction<F, O>): FlatMapRestAction<F, S, O> =
        FlatMapRestAction(callback, this)

    /**
     * Transforms the body from the [response] into the successful [S] type.
     *
     * @param response The [HttpResponse] to derive the [S] object from.
     *
     * @return An object of type [S].
     */
    protected abstract fun transformBody(response: HttpResponse<*>) : S
}