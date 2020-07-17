package bvanseg.kotlincommons.net.rest

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.net.http.HttpClient

/**
 * @author Boston Vanseghi
 * @since 2.3.0
 */
object KCHttp {

    val DEFAULT_HTTP_CLIENT = HttpClient.newHttpClient()
    val jsonMapper = jacksonObjectMapper()
}