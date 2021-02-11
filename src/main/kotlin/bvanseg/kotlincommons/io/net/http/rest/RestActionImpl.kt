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
import bvanseg.kotlincommons.util.functional.Result
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.*

/**
 * An implementation of the [RestAction] class inspired by JDA (Java Discord REST-API Wrapper).
 *
 * This implementation makes use of the default [KCHttp.DEFAULT_HTTP_CLIENT] found in [KCHttp]. This implementation also
 * features an operator overload for constructor invocation in order to make use of the type [T] as a reified generic
 * parameter. This is necessary in order to construct a [TypeReference], which persists the full generic parameter at
 * runtime, allowing support for complex generic parameter types such as [Collection]s.
 *
 * @param request The [HttpRequest] the [RestAction] will act upon.
 * @param type The generic parameter [T] as a [Class] object.
 * @param typeReference The special Jackson class used to persist complex generic parameters at runtime.
 *
 * @author Boston Vanseghi
 * @since 2.3.0
 */
@Suppress("UNCHECKED_CAST")
open class RestActionImpl<S>(
    private val request: HttpRequest,
    private val type: Class<S>,
    private val typeReference: TypeReference<S>,
    private val client: HttpClient = KotlinCommons.KC_HTTP_CLIENT,
    private val mapper: ObjectMapper = KotlinCommons.KC_JACKSON_OBJECT_MAPPER
) : RestAction<RestActionFailure, S>() {

    companion object {

        val logger = getLogger()

        inline operator fun <reified S> invoke(request: HttpRequest): RestActionImpl<S> {
            return RestActionImpl(request, S::class.java, jacksonTypeRef())
        }
    }

    private val bodyHandlerType = when (type) {
        ByteArray::class.java -> HttpResponse.BodyHandlers.ofByteArray()
        else -> HttpResponse.BodyHandlers.ofString()
    }

    override fun queueImpl(callback: (Result<RestActionFailure, S>) -> Unit): RestActionImpl<S> {
        future = client.sendAsync(request, bodyHandlerType).whenComplete { response, throwable ->
            try {

                throwable?.let { e ->
                    callback(Result.Failure(RestActionFailure(httpResponse = response, throwable = e)))
                    return@whenComplete
                }

                // Invoked no matter what.
                if (response.statusCode() in 400..599) {
                    callback(Result.Failure(RestActionFailure(httpResponse = response)))
                } else {
                    successCallback?.invoke(response)
                }

                // Fine to invoke whether or not the body is empty.
                if (type == HttpResponse::class.java) {
                    // If the type needed is an HttpResponse, the response itself can be used in the queue callback.
                    callback(Result.Success(response as S))
                    return@whenComplete
                } else if (type == String::class.java || type == ByteArray::class.java) {
                    // If the type needed is a String, the body itself can be returned as a String.
                    callback(Result.Success(response.body() as S))
                    return@whenComplete
                }

                val strBody = response.body() as String

                if (strBody.isNotEmpty()) {
                    callback(Result.Success(mapper.readValue(strBody, typeReference)))
                } else if (type == Optional::class.java) {
                    // If the type needed is a String, the body itself can be returned as a String.
                    callback(Result.Success(Optional.empty<Any>() as S))
                }
            } catch (e: Exception) {
                callback(Result.Failure(RestActionFailure(httpResponse = response, throwable = e)))
            }
        }

        return this
    }

    override fun completeImpl(): Result<RestActionFailure, S> {
        val response = try {
            client.send(request, bodyHandlerType)
        } catch (e: Exception) {
            return Result.Failure(RestActionFailure(throwable = e))
        }

        // Invoked no matter what.
        if (response.statusCode() in 400..599) {
            return Result.Failure(RestActionFailure(httpResponse = response))
        } else {
            successCallback?.invoke(response)
        }

        lateinit var result: Result.Failure<Throwable>

        try {
            // Fine to invoke whether or not the body is empty.
            if (type == HttpResponse::class.java) {
                // If the type needed is an HttpResponse, the response itself can be used in the queue callback.
                return Result.Success(response as S)
            } else if (type == String::class.java || type == ByteArray::class.java) {
                // If the type needed is a String, the body itself can be returned as a String.
                return Result.Success(response.body() as S)
            }

            val strBody = response.body() as String

            if (strBody.isNotEmpty()) {
                return Result.Success(mapper.readValue(strBody, typeReference))
            } else if (type == Optional::class.java) {
                // If the type needed is a String, the body itself can be returned as a String.
                return Result.Success(Optional.empty<Any>() as S)
            }
        } catch (e: Exception) {
            return Result.Failure(RestActionFailure(httpResponse = response, throwable = e))
        }

        return Result.Failure(RestActionFailure(httpResponse = response))
    }
}