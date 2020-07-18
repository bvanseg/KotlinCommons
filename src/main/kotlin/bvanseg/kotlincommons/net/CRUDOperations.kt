package bvanseg.kotlincommons.net

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.time.Duration

/**
 * @author Boston Vanseghi
 * @since 2.3.0
 */
open class CRUDOperation {
    var target: String = ""
    var timeout: Duration = Duration.ofSeconds(30L)
    var version: HttpClient.Version = HttpClient.Version.HTTP_2

    val headersMap: HashMap<String, String> = hashMapOf()
    val parametersMap: HashMap<String, String> = hashMapOf()

    fun headers(block: HashMap<String, String>.() -> Unit): HashMap<String, String> {
        val headers = hashMapOf<String, String>()
        headers.block()
        this.headersMap.putAll(headers)
        return headers
    }

    fun parameters(block: HashMap<String, String>.() -> Unit): HashMap<String, String> {
        val parameters = hashMapOf<String, String>()
        parameters.block()
        this.parametersMap.putAll(parameters)
        return parameters
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