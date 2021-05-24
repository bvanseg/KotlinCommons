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

import bvanseg.kotlincommons.util.functional.Result
import java.net.http.HttpResponse

/**
 * An expansion of [RestAction] that allows for chaining [RestAction] queues.
 *
 * @author Boston Vanseghi
 * @since 2.3.0
 */
class FlatMapRestAction<F, S, O>(
    val callback: (Result<F, S>) -> RestAction<F, O>?,
    override val type: Class<O>,
    private val parent: RestAction<F, S>
) : RestAction<F, O>(parent.requestBuilder, type, parent.client) {

    override fun queueImpl(callback: (Result<F, O>) -> Unit): FlatMapRestAction<F, S, O> {
        parent.queue {
            val resultAction = this.callback(it)
            // If the parent was unsuccessful, short-circuit the flatmap and return the failing parent result.
            // If the parent was successful, proceed with the callback rest action. If the provided rest action was null,
            // short-circuit the flatmap and return a failure.
            if (it.isSuccess) {
                resultAction?.queue { result ->
                    callback(result)
                } ?: callback(Result.Failure(parent.constructFailure()))
            } else {
                callback(it as Result.Failure<F>)
            }
        }

        return this
    }

    override fun completeImpl(): Result<F, O> {
        val value = parent.complete()
        // If the parent was unsuccessful, short-circuit the flatmap and return the failing parent result.
        // If the parent was successful, proceed with the callback rest action. If the provided rest action was null,
        // short-circuit the flatmap and return a failure.
        return if (value.isSuccess) {
            callback(value)?.complete() ?: Result.Failure(parent.constructFailure())
        } else {
            (value as Result.Failure<F>)
        }
    }

    // A flatmap rest action should never do transformations.
    override fun transformBody(response: HttpResponse<*>): O = throw UnsupportedOperationException()

    override fun constructFailure(response: HttpResponse<*>?, throwable: Throwable?): F =
        parent.constructFailure(response, throwable)
}