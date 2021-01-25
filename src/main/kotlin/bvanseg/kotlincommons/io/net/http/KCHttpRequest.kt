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

import java.net.URI
import java.net.http.HttpRequest

/**
 * @author Boston Vanseghi
 * @since 2.3.0
 */
class KCHttpRequest {

    val requestBuilder: HttpRequest.Builder = HttpRequest.newBuilder()

    var targetBuilder = StringBuilder()

    private fun <T : CRUDOperation> crud(op: CRUDOperation): T {

        requestBuilder.apply {
            targetBuilder.append(op.target)

            if (op.parameters.get().isNotEmpty())
                targetBuilder.append("?")

            targetBuilder.append(op.parameters.get().entries.joinToString("&") { (key, value) -> "${key}=${value}" })

            requestBuilder.uri(URI.create(targetBuilder.toString())).timeout(op.timeout).version(op.version)
        }

        for (header in op.headers.get()) {
            requestBuilder.apply { requestBuilder.setHeader(header.key, header.value) }
        }

        return op as T
    }

    fun get(block: GET.() -> Unit): GET {
        val op = GET()
        op.block()

        requestBuilder.apply { requestBuilder.GET() }

        return crud(op)
    }

    fun delete(block: DELETE.() -> Unit): DELETE {
        val op = DELETE()
        op.block()

        requestBuilder.apply { requestBuilder.DELETE() }

        return crud(op)
    }

    fun post(block: POST.() -> Unit): POST {
        val op = POST()
        op.block()

        requestBuilder.apply { requestBuilder.POST(op.publisher) }

        return crud(op)
    }

    fun put(block: PUT.() -> Unit): PUT {
        val op = PUT()
        op.block()

        requestBuilder.apply { requestBuilder.PUT(op.publisher) }

        return crud(op)
    }

    fun patch(block: PATCH.() -> Unit): PATCH {
        val op = PATCH()
        op.block()

        requestBuilder.apply { requestBuilder.PATCH(op.publisher) }

        return crud(op)
    }

    @Deprecated(
        "HTTP Requests can not be nested!", level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("error(\"...\")")
    )
    fun httpRequest(op: KCHttpRequest.() -> Unit): KCHttpRequest = error("...")
}