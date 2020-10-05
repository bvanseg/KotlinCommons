/*
 * MIT License
 *
 * Copyright (c) 2020 Boston Vanseghi
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
package bvanseg.kotlincommons.net.http

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.time.Duration

/**
 * @author Boston Vanseghi
 * @since 2.3.0
 */

class Headers {
    val map: HashMap<String, String> = hashMapOf()

    infix fun String.to(value: String) {
        map[this] = value
    }
}

class Parameters {
    val map: HashMap<String, String> = hashMapOf()

    infix fun String.to(value: String) {
        map[this] = value
    }
}

open class CRUDOperation {
    var target: String = ""
    var timeout: Duration = Duration.ofSeconds(30L)
    var version: HttpClient.Version = HttpClient.Version.HTTP_2

    val headers = Headers()
    val parameters = Parameters()

    fun headers(block: Headers.() -> Unit) {
        headers.apply(block)
    }

    fun parameters(block: Parameters.() -> Unit) {
        parameters.apply(block)
    }

    @Deprecated("CRUD operations can not be nested!", level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("error(\"...\")")
    )
    fun get(op: GET.() -> Unit): GET = error("...")

    @Deprecated("CRUD operations can not be nested!", level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("error(\"...\")")
    )
    fun delete(op: DELETE.() -> Unit): DELETE = error("...")

    @Deprecated("CRUD operations can not be nested!", level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("error(\"...\")")
    )
    fun post(op: POST.() -> Unit): POST = error("...")

    @Deprecated("CRUD operations can not be nested!", level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("error(\"...\")")
    )
    fun put(op: PUT.() -> Unit): PUT = error("...")

    @Deprecated("CRUD operations can not be nested!", level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("error(\"...\")")
    )
    fun patch(op: PATCH.() -> Unit): PATCH = error("...")

}

/**
 * @author Boston Vanseghi
 * @since 2.3.0
 */
class GET: CRUDOperation()

/**
 * @author Boston Vanseghi
 * @since 2.3.0
 */
class DELETE: CRUDOperation()

/**
 * @author Boston Vanseghi
 * @since 2.3.0
 */
open class UpdateOperation(
    var publisher: HttpRequest.BodyPublisher = HttpRequest.BodyPublishers.noBody()
): CRUDOperation()

/**
 * @author Boston Vanseghi
 * @since 2.3.0
 */
class POST: UpdateOperation()

/**
 * @author Boston Vanseghi
 * @since 2.3.0
 */
class PUT: UpdateOperation()

/**
 * @author Boston Vanseghi
 * @since 2.3.0
 */
class PATCH: UpdateOperation()