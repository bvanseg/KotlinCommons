package bvanseg.kotlincommons.io.net.http.rest.api.request

/**
 * @author Boston Vanseghi
 * @since 2.11.0
 */
class DeleteRequest(
    headers: Map<String, String> = emptyMap(),
    pathVariables: Array<String> = emptyArray(),
    queryParameters: Map<String, Any> = emptyMap()
) : RestRequest(HttpMethod.DELETE, headers, pathVariables, queryParameters)