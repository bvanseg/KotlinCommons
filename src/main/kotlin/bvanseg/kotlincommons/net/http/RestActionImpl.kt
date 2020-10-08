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

import bvanseg.kotlincommons.any.getLogger
import com.fasterxml.jackson.core.type.TypeReference
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
open class RestActionImpl<T>(private val request: HttpRequest, private val type: Class<T>, private val typeReference: TypeReference<T>, private val client: HttpClient = KCHttp.DEFAULT_HTTP_CLIENT): RestAction<T>() {

    companion object {

        val logger = getLogger()

        inline operator fun <reified T : Any>invoke(request: HttpRequest): RestActionImpl<T> {
            return RestActionImpl(request, T::class.java, jacksonTypeRef())
        }
    }

    init {
        this.exceptionCallback = {
            logger.error("An exception has occurred while executing a RestAction", it)
        }
    }

    override fun queueImpl() {
        client.sendAsync(request, HttpResponse.BodyHandlers.discarding()).whenComplete { response, throwable ->
            if(response.statusCode() in 400..599) {
                errorCallback?.invoke(response)
            } else {
                successCallback?.invoke(response)
            }

            throwable?.let { e ->
                exceptionCallback?.invoke(e)
            }
        }
    }

    override fun queueImpl(callback: (T) -> Unit) {
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).whenComplete { response, throwable ->
            try {
                // Invoked no matter what.
                if(response.statusCode() in 400..599) {
                    errorCallback?.invoke(response)
                } else {
                    successCallback?.invoke(response)
                }

                // Fine to invoke whether or not the body is empty.
                if(type == HttpResponse::class.java) {
                    // If the type needed is an HttpResponse, the response itself can be used in the queue callback.
                    callback(response as T)
                    return@whenComplete
                } else if(type == String::class.java) {
                    // If the type needed is a String, the body itself can be returned as a String.
                    callback(response.body() as T)
                    return@whenComplete
                }

                if(response.body().isNotEmpty()) {
                    callback(KCHttp.jsonMapper.readValue(response.body(), typeReference))
                } else if(type == Optional::class.java) {
                    // If the type needed is a String, the body itself can be returned as a String.
                    callback(Optional.empty<Any>() as T)
                }
            } catch (e: Exception) {
                exceptionCallback?.invoke(e)
            }

            throwable?.let { e ->
                exceptionCallback?.invoke(e)
            }
        }
    }

    override fun completeImpl(): T? {
        val response = try {
            client.send(request, HttpResponse.BodyHandlers.ofString())
        } catch (e: Exception) {
            exceptionCallback?.invoke(e)
            return null
        }

        // Invoked no matter what.
        if(response.statusCode() in 400..599) {
            errorCallback?.invoke(response)
        } else {
            successCallback?.invoke(response)
        }

        try {
            // Fine to invoke whether or not the body is empty.
            if(type == HttpResponse::class.java) {
                // If the type needed is an HttpResponse, the response itself can be used in the queue callback.
                return response as T
            } else if(type == String::class.java) {
                // If the type needed is a String, the body itself can be returned as a String.
                return response.body() as T
            }

            if(response.body().isNotEmpty()) {
                return KCHttp.jsonMapper.readValue(response.body(), typeReference)
            } else if(type == Optional::class.java) {
                // If the type needed is a String, the body itself can be returned as a String.
                return Optional.empty<Any>() as T
            }
        } catch (e: Exception) {
            exceptionCallback?.invoke(e)
        }

        return null
    }
}