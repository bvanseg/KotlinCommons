package bvanseg.kotlincommons.net

import java.net.URI
import java.net.http.HttpRequest

/**
 * @author Boston Vanseghi
 * @since 2.3.0
 */
class KCHttpRequest {

    val requestBuilder: HttpRequest.Builder = HttpRequest.newBuilder()

    var targetBuilder = StringBuilder()

    private fun <T: CRUDOperation> crud(op: CRUDOperation): T {

        requestBuilder.apply {
            targetBuilder.append(op.target)

            if(op.parameters.map.isNotEmpty())
                targetBuilder.append("?")

            targetBuilder.append(op.parameters.map.entries.joinToString("&") { (key, value) -> "${key}=${value}" })

            requestBuilder.uri(URI.create(targetBuilder.toString())).timeout(op.timeout).version(op.version)
        }

        for(header in op.headers.map) {
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

    @Deprecated("HTTP Requests can not be nested!", level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("error(\"...\")")
    )
    fun httpRequest(op: KCHttpRequest.() -> Unit): KCHttpRequest = error("...")
}