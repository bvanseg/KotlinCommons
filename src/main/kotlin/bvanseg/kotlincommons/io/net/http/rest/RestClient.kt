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
import bvanseg.kotlincommons.io.net.http.rest.endpoint.DeleteEndpoint
import bvanseg.kotlincommons.io.net.http.rest.endpoint.Endpoint
import bvanseg.kotlincommons.io.net.http.rest.endpoint.GetEndpoint
import bvanseg.kotlincommons.io.net.http.rest.endpoint.PatchEndpoint
import bvanseg.kotlincommons.io.net.http.rest.endpoint.PostEndpoint
import bvanseg.kotlincommons.io.net.http.rest.endpoint.PutEndpoint
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import java.net.http.HttpClient

/**
 * @author Boston Vanseghi
 * @since 2.11.0
 */
abstract class RestClient(
    val baseURL: String,
    val httpClient: HttpClient = KotlinCommons.KC_HTTP_CLIENT,
    val jsonMapper: ObjectMapper = KotlinCommons.KC_JACKSON_OBJECT_MAPPER
) {

    companion object {
        private val logger = getLogger()
    }

    val defaultHeaders = hashMapOf<String, String>()
    val defaultParameters = hashMapOf<String, String>()

    open val defaultFailure: (RestActionFailure) -> Unit = {
        when (it) {
            is ThrowableFailure -> it.throwable.printStackTrace()
            is ResponseFailure -> it.response.apply {
                logger.error("Received erroring status code ${statusCode()} from endpoint '${request().uri()}', body: ${body()}")
            }
        }
    }

    protected inline fun <reified T> createEndpoint(path: String): Endpoint<T> =
        Endpoint(this, path, T::class.java, jacksonTypeRef())

    protected inline fun <reified T> createDeleteEndpoint(path: String): DeleteEndpoint<T> =
        Endpoint(this, path, T::class.java, jacksonTypeRef())

    protected inline fun <reified T> createGetEndpoint(path: String): GetEndpoint<T> =
        Endpoint(this, path, T::class.java, jacksonTypeRef())

    protected inline fun <reified T> createPatchEndpoint(path: String): PatchEndpoint<T> =
        Endpoint(this, path, T::class.java, jacksonTypeRef())

    protected inline fun <reified T> createPostEndpoint(path: String): PostEndpoint<T> =
        Endpoint(this, path, T::class.java, jacksonTypeRef())

    protected inline fun <reified T> createPutEndpoint(path: String): PutEndpoint<T> =
        Endpoint(this, path, T::class.java, jacksonTypeRef())
}