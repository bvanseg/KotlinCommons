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

import bvanseg.kotlincommons.io.net.http.rest.RestClient
import bvanseg.kotlincommons.io.net.http.rest.impl.RestActionImpl
import bvanseg.kotlincommons.io.net.http.rest.request.DeleteRequest
import bvanseg.kotlincommons.io.net.http.rest.request.GetRequest
import bvanseg.kotlincommons.io.net.http.rest.request.PatchRequest
import bvanseg.kotlincommons.io.net.http.rest.request.PostRequest
import bvanseg.kotlincommons.io.net.http.rest.request.PutRequest
import bvanseg.kotlincommons.io.net.http.rest.request.RestRequest
import com.fasterxml.jackson.core.type.TypeReference

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

    private fun buildRestActionImpl(request: RestRequest): RestActionImpl<T> {
        val defaultRequest =
            RestRequest(
                request.httpMethod,
                headers = restClient.defaultHeaders,
                queryParameters = restClient.defaultParameters
            )
        val fullRequest = defaultRequest.combine(request)
        val requestBuilder = fullRequest.toBuilder(fullPath, restClient.jsonMapper)
        return RestActionImpl(
            requestBuilder, type, typeReference, client = restClient.httpClient, mapper = restClient.jsonMapper
        ).onFailure(restClient.defaultFailure) as RestActionImpl<T>
    }

    override fun delete(restRequest: DeleteRequest): RestActionImpl<T> = buildRestActionImpl(restRequest)
    override fun get(restRequest: GetRequest): RestActionImpl<T> = buildRestActionImpl(restRequest)
    override fun patch(restRequest: PatchRequest): RestActionImpl<T> = buildRestActionImpl(restRequest)
    override fun post(restRequest: PostRequest): RestActionImpl<T> = buildRestActionImpl(restRequest)
    override fun put(restRequest: PutRequest): RestActionImpl<T> = buildRestActionImpl(restRequest)
}