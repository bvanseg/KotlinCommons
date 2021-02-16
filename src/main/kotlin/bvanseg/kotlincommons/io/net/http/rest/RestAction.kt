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

import bvanseg.kotlincommons.KotlinCommons
import bvanseg.kotlincommons.io.logging.getLogger
import bvanseg.kotlincommons.time.api.Khrono
import bvanseg.kotlincommons.util.any.delay
import bvanseg.kotlincommons.util.functional.Result
import com.fasterxml.jackson.core.type.TypeReference
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.http.HttpClient
import java.net.http.HttpRequest
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
abstract class RestAction<F, S>(
    open val request: HttpRequest,
    open val type: Class<S>,
    open val client: HttpClient = KotlinCommons.KC_HTTP_CLIENT,
    private val bodyHandlerType: HttpResponse.BodyHandler<*> = when (type) {
        ByteArray::class.java -> HttpResponse.BodyHandlers.ofByteArray()
        else -> HttpResponse.BodyHandlers.ofString()
    }
) {

    companion object {
        private val logger = getLogger()
    }

    var future: CompletableFuture<out HttpResponse<*>>? = null
        protected set

    protected var failureCallback: ((F) -> Unit)? = null
    protected var successCallback: ((S) -> Unit)? = null

    open fun onFailure(callback: (F) -> Unit): RestAction<F, S> {
        failureCallback = callback
        return this
    }

    open fun onSuccess(callback: (S) -> Unit): RestAction<F, S> {
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

    protected open fun queueImpl(callback: (Result<F, S>) -> Unit = {}): RestAction<F, S> {
        future = client.sendAsync(request, bodyHandlerType).whenComplete { response, throwable ->
            try {
                throwable?.let { e ->
                    val failure = constructFailure(response = response, throwable = e)
                    failureCallback?.invoke(failure)
                    callback(Result.Failure(failure))
                    return@whenComplete
                }

                if (response.statusCode() in 400..599) {
                    val failure = constructFailure(response = response)
                    failureCallback?.invoke(failure)
                    callback(Result.Failure(failure))
                    return@whenComplete
                }

                val successObject = transformBody(response)
                successCallback?.invoke(successObject)
                callback(Result.Success(successObject))
            } catch (e: Exception) {
                val failure = constructFailure(response = response, throwable = e)
                failureCallback?.invoke(failure)
                callback(Result.Failure(failure))
            }
        }

        return this
    }

    protected open fun completeImpl(): Result<F, S> {
        val response = try {
            client.send(request, bodyHandlerType)
        } catch (e: Exception) {
            val failure = constructFailure(throwable = e)
            failureCallback?.invoke(failure)
            return Result.Failure(failure)
        }

        if (response.statusCode() in 400..599) {
            val failure = constructFailure(response = response)
            failureCallback?.invoke(failure)
            return Result.Failure(failure)
        }

        return try {
            val successObject = transformBody(response)
            successCallback?.invoke(successObject)
            Result.Success(successObject)
        } catch (e: Exception) {
            val failure = constructFailure(response = response, throwable = e)
            failureCallback?.invoke(failure)
            Result.Failure(failure)
        }
    }

    inline fun <reified O> flatMap(noinline callback: (Result<F, S>) -> RestAction<F, O>?): FlatMapRestAction<F, S, O> =
        FlatMapRestAction(callback, O::class.java, this)

    /**
     * Transforms the body from the [response] into the successful [S] type.
     *
     * @param response The [HttpResponse] to derive the [S] object from.
     *
     * @return An object of type [S].
     */
    protected abstract fun transformBody(response: HttpResponse<*>) : S

    abstract fun constructFailure(response: HttpResponse<*>? = null, throwable: Throwable? = null): F
}