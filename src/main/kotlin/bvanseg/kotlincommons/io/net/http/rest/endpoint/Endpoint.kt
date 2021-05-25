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
package bvanseg.kotlincommons.io.net.http.rest.endpoint

import bvanseg.kotlincommons.io.net.http.HttpMethod
import bvanseg.kotlincommons.io.net.http.KCHttpRequestBuilder
import bvanseg.kotlincommons.io.net.http.httpRequestBuilder
import bvanseg.kotlincommons.io.net.http.rest.RestClient
import bvanseg.kotlincommons.io.net.http.rest.impl.RestActionImpl
import com.fasterxml.jackson.core.type.TypeReference
import java.net.http.HttpRequest

/**
 * @author Boston Vanseghi
 * @since 2.11.0
 */
class Endpoint<T>(
    val restClient: RestClient,
    path: String,
    val type: Class<T>,
    private val typeReference: TypeReference<T>
) : DeleteEndpoint<T>, GetEndpoint<T>, PatchEndpoint<T>, PostEndpoint<T>, PutEndpoint<T> {
    val fullPath = restClient.baseURL + path

    private fun buildRestActionImpl(
        httpMethod: HttpMethod,
        body: Any?,
        callback: KCHttpRequestBuilder.() -> Unit = {}
    ): RestActionImpl<T> {
        val requestBuilder = httpRequestBuilder(fullPath).apply(restClient.defaultBuilderCallback)
        val bodyPublisher = body?.let { restClient.factory.bodyPublisherCallback(body) }
            ?: HttpRequest.BodyPublishers.noBody()
        when (httpMethod) {
            HttpMethod.DELETE -> requestBuilder.delete(callback)
            HttpMethod.GET -> requestBuilder.get(callback)
            HttpMethod.PATCH -> requestBuilder.patch(bodyPublisher, callback)
            HttpMethod.POST -> requestBuilder.post(bodyPublisher, callback)
            HttpMethod.PUT -> requestBuilder.put(bodyPublisher, callback)
        }
        val rest = restClient.factory.create(requestBuilder, type, typeReference)
        return rest.onFailure(restClient.defaultFailure) as RestActionImpl<T>
    }

    override fun delete(callback: KCHttpRequestBuilder.() -> Unit): RestActionImpl<T> =
        buildRestActionImpl(HttpMethod.DELETE, null, callback)

    override fun get(callback: KCHttpRequestBuilder.() -> Unit): RestActionImpl<T> =
        buildRestActionImpl(HttpMethod.GET, null, callback)

    override fun patch(body: Any?, callback: KCHttpRequestBuilder.() -> Unit): RestActionImpl<T> =
        buildRestActionImpl(HttpMethod.PATCH, body, callback)

    override fun post(body: Any?, callback: KCHttpRequestBuilder.() -> Unit): RestActionImpl<T> =
        buildRestActionImpl(HttpMethod.POST, body, callback)

    override fun put(body: Any?, callback: KCHttpRequestBuilder.() -> Unit): RestActionImpl<T> =
        buildRestActionImpl(HttpMethod.PUT, body, callback)
}