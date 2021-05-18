package bvanseg.kotlincommons.io.net.http.rest.api.request

import bvanseg.kotlincommons.io.net.http.PATCH
import bvanseg.kotlincommons.io.net.http.QueryHandler
import bvanseg.kotlincommons.lang.string.toURI
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
    val requestBody: Any? = null
) {

    fun toHttpRequest(uri: String, mapper: ObjectMapper): HttpRequest {
        val url = uri.format(pathVariables) + QueryHandler.build(queryParameters)
        val builder = HttpRequest.newBuilder(url.toURI())

        when (httpMethod) {
            HttpMethod.DELETE -> builder.DELETE()
            HttpMethod.GET -> builder.GET()
            HttpMethod.PATCH -> builder.PATCH(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(requestBody)))
            HttpMethod.POST -> builder.POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(requestBody)))
            HttpMethod.PUT -> builder.PUT(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(requestBody)))
        }

        headers.forEach {
            builder.setHeader(it.key, it.value)
        }

        return builder.build()
    }

    fun combine(request: RestRequest) = RestRequest(
        httpMethod,
        headers + request.headers,
        pathVariables + request.pathVariables,
        queryParameters + request.queryParameters,
        request.requestBody
    )
}
