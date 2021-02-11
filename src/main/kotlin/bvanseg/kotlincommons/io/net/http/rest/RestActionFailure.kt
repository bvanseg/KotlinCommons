package bvanseg.kotlincommons.io.net.http.rest

import java.net.http.HttpResponse

/**
 * @author Boston Vanseghi
 * @since 2.9.0
 */
data class RestActionFailure(
    val httpResponse: HttpResponse<*>? = null,
    val throwable: Throwable? = null
)
