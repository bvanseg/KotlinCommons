package bvanseg.kotlincommons.io.net.http.rest

import bvanseg.kotlincommons.KotlinCommons
import bvanseg.kotlincommons.io.logging.getLogger
import bvanseg.kotlincommons.io.net.http.rest.endpoint.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import java.net.http.HttpClient

/**
 * @author Boston Vanseghi
 * @since 2.11.0
 */
abstract class RestAPI(
    val baseURL: String,
    val httpClient: HttpClient = KotlinCommons.KC_HTTP_CLIENT,
    val jsonMapper: ObjectMapper = KotlinCommons.KC_JACKSON_OBJECT_MAPPER
) {

    companion object {
        private val logger = getLogger()
    }

    val defaultHeaders = hashMapOf<String, String>()
    val defaultParameters = hashMapOf<String, String>()

    open val defaultFailure: (RestActionFailure) -> Unit = {
        when (it) {
            is ThrowableFailure -> it.throwable.printStackTrace()
            is ResponseFailure -> it.response.apply {
                logger.error("Received erroring status code ${statusCode()} from endpoint '${request().uri()}', body: ${body()}")
            }
        }
    }

    protected inline fun <reified T> createEndpoint(path: String): Endpoint<T> =
        Endpoint(this, path, T::class.java, jacksonTypeRef())

    protected inline fun <reified T> createDeleteEndpoint(path: String): DeleteEndpoint<T> =
        Endpoint(this, path, T::class.java, jacksonTypeRef())

    protected inline fun <reified T> createGetEndpoint(path: String): GetEndpoint<T> =
        Endpoint(this, path, T::class.java, jacksonTypeRef())

    protected inline fun <reified T> createPatchEndpoint(path: String): PatchEndpoint<T> =
        Endpoint(this, path, T::class.java, jacksonTypeRef())

    protected inline fun <reified T> createPostEndpoint(path: String): PostEndpoint<T> =
        Endpoint(this, path, T::class.java, jacksonTypeRef())

    protected inline fun <reified T> createPutEndpoint(path: String): PutEndpoint<T> =
        Endpoint(this, path, T::class.java, jacksonTypeRef())
}