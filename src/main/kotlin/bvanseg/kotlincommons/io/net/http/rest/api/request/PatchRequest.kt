package bvanseg.kotlincommons.io.net.http.rest.api.request

/**
 * @author Boston Vanseghi
 * @since 2.11.0
 */
class PatchRequest(
    headers: Map<String, String> = emptyMap(),
    pathVariables: Array<String> = emptyArray(),
    queryParameters: Map<String, Any> = emptyMap(),
    requestBody: Any? = null
) : RestRequest(HttpMethod.PATCH, headers, pathVariables, queryParameters, requestBody)