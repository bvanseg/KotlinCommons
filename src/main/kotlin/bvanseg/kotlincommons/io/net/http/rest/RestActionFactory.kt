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

import bvanseg.kotlincommons.io.net.http.KCHttpRequestBuilder
import bvanseg.kotlincommons.io.net.http.httpRequestBuilder
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import java.net.http.HttpClient
import java.net.http.HttpRequest

/**
 * @author Boston Vanseghi
 */
abstract class RestActionFactory<F>(
    val client: HttpClient,
    val mapper: ObjectMapper,
    val bodyPublisherCallback: (Any?) -> HttpRequest.BodyPublisher = { HttpRequest.BodyPublishers.noBody() },
    val defaultHeaders: Map<String, String> = emptyMap(),
    val defaultParameters: Map<String, String> = emptyMap()
) {

    abstract fun <S> create(
        requestBuilder: KCHttpRequestBuilder,
        type: Class<S>,
        typeReference: TypeReference<S>
    ): RestAction<F, S>

    inline fun <reified S> create(requestBuilder: KCHttpRequestBuilder) =
        create(requestBuilder, S::class.java, jacksonTypeRef())

    private val builderCallback: KCHttpRequestBuilder.() -> Unit = {
        headers.putAll(defaultHeaders)
        parameters.putAll(defaultParameters)
    }

    fun <S> delete(
        target: String,
        type: Class<S>,
        typeReference: TypeReference<S>
    ): RestAction<F, S> = create(httpRequestBuilder(target).delete(builderCallback), type, typeReference)

    inline fun <reified S> delete(target: String) = delete(target, S::class.java, jacksonTypeRef())

    fun <S> get(
        target: String,
        type: Class<S>,
        typeReference: TypeReference<S>
    ): RestAction<F, S> = create(httpRequestBuilder(target).get(builderCallback), type, typeReference)

    inline fun <reified S> get(target: String) = get(target, S::class.java, jacksonTypeRef())

    fun <S> patch(
        target: String,
        type: Class<S>,
        typeReference: TypeReference<S>,
        body: Any? = null
    ): RestAction<F, S> = create(
        httpRequestBuilder(target).patch(bodyPublisherCallback(body), builderCallback),
        type,
        typeReference
    )

    inline fun <reified S> patch(target: String, body: Any? = null) =
        patch(target, S::class.java, jacksonTypeRef(), body)

    fun <S> post(
        target: String,
        type: Class<S>,
        typeReference: TypeReference<S>,
        body: Any? = null
    ): RestAction<F, S> = create(
        httpRequestBuilder(target).post(bodyPublisherCallback(body), builderCallback),
        type,
        typeReference
    )

    inline fun <reified S> post(target: String, body: Any? = null) = post(target, S::class.java, jacksonTypeRef(), body)

    fun <S> put(
        target: String,
        type: Class<S>,
        typeReference: TypeReference<S>,
        body: Any? = null
    ): RestAction<F, S> = create(
        httpRequestBuilder(target).put(bodyPublisherCallback(body), builderCallback),
        type,
        typeReference
    )

    inline fun <reified S> put(target: String, body: Any? = null) = put(target, S::class.java, jacksonTypeRef(), body)
}