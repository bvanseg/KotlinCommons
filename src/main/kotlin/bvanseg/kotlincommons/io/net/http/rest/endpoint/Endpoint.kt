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

import bvanseg.kotlincommons.io.net.http.rest.RestAPI
import bvanseg.kotlincommons.io.net.http.rest.RestActionImpl
import bvanseg.kotlincommons.io.net.http.rest.request.*
import com.fasterxml.jackson.core.type.TypeReference

/**
 * @author Boston Vanseghi
 * @since 2.11.0
 */
class Endpoint<T>(
    val restApi: RestAPI,
    path: String,
    val type: Class<T>,
    private val typeReference: TypeReference<T>
) : DeleteEndpoint<T>, GetEndpoint<T>, PatchEndpoint<T>, PostEndpoint<T>, PutEndpoint<T> {
    val fullPath = restApi.baseURL + path

    private fun buildRestActionImpl(request: RestRequest): RestActionImpl<T> {
        val defaultRequest =
            RestRequest(
                request.httpMethod,
                headers = restApi.defaultHeaders,
                queryParameters = restApi.defaultParameters
            )
        val fullRequest = defaultRequest.combine(request)
        val httpRequest = fullRequest.toHttpRequest(fullPath, restApi.jsonMapper)
        return RestActionImpl(
            httpRequest, type, typeReference, client = restApi.httpClient, mapper = restApi.jsonMapper
        ).onFailure(restApi.defaultFailure) as RestActionImpl<T>
    }

    override fun delete(restRequest: DeleteRequest): RestActionImpl<T> = buildRestActionImpl(restRequest)
    override fun get(restRequest: GetRequest): RestActionImpl<T> = buildRestActionImpl(restRequest)
    override fun patch(restRequest: PatchRequest): RestActionImpl<T> = buildRestActionImpl(restRequest)
    override fun post(restRequest: PostRequest): RestActionImpl<T> = buildRestActionImpl(restRequest)
    override fun put(restRequest: PutRequest): RestActionImpl<T> = buildRestActionImpl(restRequest)
}