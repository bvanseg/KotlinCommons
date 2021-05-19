package bvanseg.kotlincommons.io.net.http.rest.request

import java.time.Duration

/**
 * @author Boston Vanseghi
 * @since 2.11.0
 */
class GetRequest(
    headers: Map<String, String> = emptyMap(),
    pathVariables: Array<String> = emptyArray(),
    queryParameters: Map<String, Any> = emptyMap(),
    timeout: Duration = Duration.ofSeconds(30L)
) : RestRequest(HttpMethod.GET, headers, pathVariables, queryParameters, timeout = timeout)