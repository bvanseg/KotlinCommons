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

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import java.net.http.HttpRequest
import java.net.http.HttpResponse

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
class RestActionImpl<T>(private val request: HttpRequest, private val type: Class<T>, private val typeReference: TypeReference<T>): RestAction<T>() {

    companion object {
        inline operator fun <reified T : Any>invoke(request: HttpRequest): RestActionImpl<T> {
            return RestActionImpl(request, T::class.java, jacksonTypeRef())
        }
    }


    override fun queueImpl() {
        KCHttp.DEFAULT_HTTP_CLIENT.sendAsync(request, HttpResponse.BodyHandlers.discarding()).thenAcceptAsync {
            successCallback?.invoke(it)
        }
    }

    override fun queueImpl(callback: (T) -> Unit) {
        KCHttp.DEFAULT_HTTP_CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAcceptAsync { response ->
            try {
                if (response.body().isNotEmpty() && type != HttpResponse::class.java && type != String::class.java) {
                    callback(KCHttp.jsonMapper.readValue(response.body(), typeReference))
                } else if(type == HttpResponse::class.java) {
                    callback(response as T)
                } else if(type == String::class.java) {
                    callback(response.body() as T)
                }

                successCallback?.invoke(response)
            } catch (e: Exception) {
                e.printStackTrace()
                exceptionCallback?.invoke(e)
            }
        }
    }

    override fun completeImpl(): T {
        val response = KCHttp.DEFAULT_HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString())

        return if (response.body().isNotEmpty())
            KCHttp.jsonMapper.readValue(response.body(), typeReference)
        else
            response as T
    }
}