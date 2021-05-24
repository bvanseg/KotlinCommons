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
package bvanseg.kotlincommons.io.net.http.rest.request

import bvanseg.kotlincommons.io.net.http.HttpMethod
import bvanseg.kotlincommons.io.net.http.KCHttpRequestBuilder
import bvanseg.kotlincommons.io.net.http.QueryHandler
import bvanseg.kotlincommons.time.api.Khrono
import bvanseg.kotlincommons.time.api.seconds
import com.fasterxml.jackson.databind.ObjectMapper
import java.net.http.HttpRequest

/**
 * @author Boston Vanseghi
 * @since 2.11.0
 */
open class RestRequest(
    var httpMethod: HttpMethod,
    val headers: Map<String, String> = emptyMap(),
    val pathVariables: Array<String> = emptyArray(),
    val queryParameters: Map<String, Any> = emptyMap(),
    val requestBody: Any? = null,
    val timeout: Khrono = 30.seconds
) {

    fun toBuilder(uri: String, mapper: ObjectMapper): KCHttpRequestBuilder {
        val url = uri.format(*pathVariables) + QueryHandler.build(queryParameters)
        val builder = KCHttpRequestBuilder(url)

        when (httpMethod) {
            HttpMethod.DELETE -> builder.delete()
            HttpMethod.GET -> builder.get()
            HttpMethod.PATCH -> builder.patch(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(requestBody)))
            HttpMethod.POST -> builder.post(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(requestBody)))
            HttpMethod.PUT -> builder.put(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(requestBody)))
        }

        headers.forEach {
            builder.headers[it.key] = it.value
        }

        builder.timeout = timeout

        return builder
    }

    fun combine(request: RestRequest) = RestRequest(
        httpMethod,
        headers + request.headers,
        pathVariables + request.pathVariables,
        queryParameters + request.queryParameters,
        request.requestBody,
        request.timeout
    )
}
