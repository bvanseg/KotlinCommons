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

import bvanseg.kotlincommons.io.net.http.rest.RestActionImpl
import java.net.http.HttpRequest

fun HttpRequest.Builder.PATCH(publisher: HttpRequest.BodyPublisher) = this.method("PATCH", publisher)

fun httpRequest(target: String, block: KCHttpRequestBuilder.(String) -> Unit = {}): HttpRequest {
    val kcRequest = KCHttpRequestBuilder(target)
    kcRequest.block(target)
    return kcRequest.build()
}

/**
 * @author Boston Vanseghi
 * @since 2.11.0
 */
inline fun <reified T> restAction(
    target: String,
    block: KCHttpRequestBuilder.(String) -> Unit = {}
): RestActionImpl<T> {
    val kcRequest = KCHttpRequestBuilder(target)
    kcRequest.block(target)
    val request = kcRequest.build()
    return RestActionImpl(request)
}

inline fun <reified T> httpMethod(
    target: String,
    block: KCHttpRequestBuilder.(String) -> Unit = {},
    methodBlock: (KCHttpRequestBuilder) -> Unit
): RestActionImpl<T> {
    val kcRequest = KCHttpRequestBuilder(target)
    kcRequest.block(target)
    methodBlock(kcRequest)
    val request = kcRequest.build()
    return RestActionImpl(request)
}

inline fun <reified T> delete(
    target: String,
    block: KCHttpRequestBuilder.(String) -> Unit = {}
): RestActionImpl<T> = httpMethod(target, block = block) { it.delete() }

inline fun <reified T> get(
    target: String,
    block: KCHttpRequestBuilder.(String) -> Unit = {}
): RestActionImpl<T> = httpMethod(target, block = block) { it.get() }

inline fun <reified T> patch(
    target: String,
    bodyPublisher: HttpRequest.BodyPublisher = HttpRequest.BodyPublishers.noBody(),
    block: KCHttpRequestBuilder.(String) -> Unit = {}
): RestActionImpl<T> = httpMethod(target, block) { it.patch(bodyPublisher) }

inline fun <reified T> post(
    target: String,
    bodyPublisher: HttpRequest.BodyPublisher = HttpRequest.BodyPublishers.noBody(),
    block: KCHttpRequestBuilder.(String) -> Unit = {}
): RestActionImpl<T> = httpMethod(target, block) { it.post(bodyPublisher) }

inline fun <reified T> put(
    target: String,
    bodyPublisher: HttpRequest.BodyPublisher = HttpRequest.BodyPublishers.noBody(),
    block: KCHttpRequestBuilder.(String) -> Unit = {}
): RestActionImpl<T> = httpMethod(target, block) { it.put(bodyPublisher) }
