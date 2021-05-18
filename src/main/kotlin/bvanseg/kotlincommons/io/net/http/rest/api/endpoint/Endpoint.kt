package bvanseg.kotlincommons.io.net.http.rest.api.endpoint

import bvanseg.kotlincommons.io.net.http.rest.RestActionImpl
import bvanseg.kotlincommons.io.net.http.rest.api.API
import bvanseg.kotlincommons.io.net.http.rest.api.request.DeleteRequest
import bvanseg.kotlincommons.io.net.http.rest.api.request.GetRequest
import bvanseg.kotlincommons.io.net.http.rest.api.request.PatchRequest
import bvanseg.kotlincommons.io.net.http.rest.api.request.PostRequest
import bvanseg.kotlincommons.io.net.http.rest.api.request.PutRequest
import bvanseg.kotlincommons.io.net.http.rest.api.request.RestRequest
import com.fasterxml.jackson.core.type.TypeReference

/**
 * @author Boston Vanseghi
 * @since 2.11.0
 */
class Endpoint<T>(
    val api: API,
    path: String,
    val type: Class<T>,
    private val typeReference: TypeReference<T>
) : DeleteEndpoint<T>, GetEndpoint<T>, PatchEndpoint<T>, PostEndpoint<T>, PutEndpoint<T> {
    val fullPath = api.baseURL + path

    private fun buildRestActionImpl(request: RestRequest): RestActionImpl<T> {
        val defaultRequest =
            RestRequest(request.httpMethod, headers = api.defaultHeaders, queryParameters = api.defaultParameters)
        val fullRequest = defaultRequest.combine(request)
        val httpRequest = fullRequest.toHttpRequest(fullPath, api.jsonMapper)
        return RestActionImpl(
            httpRequest, type, typeReference, client = api.httpClient, mapper = api.jsonMapper
        ).onFailure(api.defaultFailure) as RestActionImpl<T>
    }

    override fun delete(restRequest: DeleteRequest): RestActionImpl<T> = buildRestActionImpl(restRequest)
    override fun get(restRequest: GetRequest): RestActionImpl<T> = buildRestActionImpl(restRequest)
    override fun patch(restRequest: PatchRequest): RestActionImpl<T> = buildRestActionImpl(restRequest)
    override fun post(restRequest: PostRequest): RestActionImpl<T> = buildRestActionImpl(restRequest)
    override fun put(restRequest: PutRequest): RestActionImpl<T> = buildRestActionImpl(restRequest)
}