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
package bvanseg.kotlincommons.io.net.http

import bvanseg.kotlincommons.grouping.collection.joinToString
import bvanseg.kotlincommons.io.net.http.rest.api.request.HttpMethod
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.time.Duration

/**
 * @author Boston Vanseghi
 * @since 2.3.0
 */
class KCHttpRequestBuilder(val target: String) {
    private val requestBuilder: HttpRequest.Builder = HttpRequest.newBuilder()
    private var targetBuilder = StringBuilder()

    var method: HttpMethod = HttpMethod.GET
        private set

    val headers = hashMapOf<String, String>()
    val parameters = hashMapOf<String, String>()

    private var bodyPublisher = HttpRequest.BodyPublishers.noBody()

    var timeout: Duration = Duration.ofSeconds(30L)
    var version: HttpClient.Version = HttpClient.Version.HTTP_2

    fun build(): HttpRequest {
        targetBuilder.append(target)

        if (parameters.isNotEmpty()) {
            targetBuilder.append("?")
        }

        headers.forEach { (key, value) ->
            requestBuilder.setHeader(key, value)
        }

        targetBuilder.append(parameters.joinToString("&") { (key, value) -> "${key}=${value}" })

        requestBuilder.uri(URI.create(targetBuilder.toString())).timeout(timeout).version(version)

        when (method) {
            HttpMethod.DELETE -> requestBuilder.DELETE()
            HttpMethod.GET -> requestBuilder.GET()
            HttpMethod.PATCH -> requestBuilder.PATCH(bodyPublisher)
            HttpMethod.POST -> requestBuilder.POST(bodyPublisher)
            HttpMethod.PUT -> requestBuilder.PUT(bodyPublisher)
        }

        return requestBuilder.build()
    }

    private fun crud(
        method: HttpMethod,
        bodyPublisher: HttpRequest.BodyPublisher,
        cb: KCHttpRequestBuilder.() -> Unit
    ) {
        this.method = method
        this.bodyPublisher = bodyPublisher
        this.apply(cb)
    }

    fun delete(cb: KCHttpRequestBuilder.() -> Unit) = crud(HttpMethod.DELETE, bodyPublisher, cb)
    fun get(cb: KCHttpRequestBuilder.() -> Unit) = crud(HttpMethod.GET, bodyPublisher, cb)
    fun patch(bodyPublisher: HttpRequest.BodyPublisher = this.bodyPublisher, cb: KCHttpRequestBuilder.() -> Unit) =
        crud(HttpMethod.PATCH, bodyPublisher, cb)

    fun post(bodyPublisher: HttpRequest.BodyPublisher = this.bodyPublisher, cb: KCHttpRequestBuilder.() -> Unit) =
        crud(HttpMethod.POST, bodyPublisher, cb)

    fun put(bodyPublisher: HttpRequest.BodyPublisher = this.bodyPublisher, cb: KCHttpRequestBuilder.() -> Unit) =
        crud(HttpMethod.PUT, bodyPublisher, cb)
}