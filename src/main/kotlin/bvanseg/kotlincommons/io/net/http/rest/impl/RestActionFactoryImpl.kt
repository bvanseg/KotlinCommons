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
package bvanseg.kotlincommons.io.net.http.rest.impl

import bvanseg.kotlincommons.KotlinCommons
import bvanseg.kotlincommons.io.net.http.KCHttpRequestBuilder
import bvanseg.kotlincommons.io.net.http.rest.RestAction
import bvanseg.kotlincommons.io.net.http.rest.RestActionFactory
import bvanseg.kotlincommons.io.net.http.rest.RestActionFailure
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import java.net.http.HttpClient

class RestActionFactoryImpl(
    httpClient: HttpClient = KotlinCommons.KC_HTTP_CLIENT,
    jsonMapper: ObjectMapper = KotlinCommons.KC_JACKSON_OBJECT_MAPPER,
    defaultParameters: Map<String, String> = emptyMap(),
    defaultHeaders: Map<String, String> = emptyMap()
) : RestActionFactory<RestActionFailure>(
    httpClient,
    jsonMapper,
    defaultHeaders = defaultHeaders,
    defaultParameters = defaultParameters
) {
    override fun <S> create(
        requestBuilder: KCHttpRequestBuilder,
        type: Class<S>,
        typeReference: TypeReference<S>
    ): RestAction<RestActionFailure, S> = RestActionImpl(requestBuilder, type, typeReference, client, mapper)
}