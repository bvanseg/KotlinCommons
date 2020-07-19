package bvanseg.kotlincommons.net

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