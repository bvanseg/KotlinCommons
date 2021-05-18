package bvanseg.kotlincommons.io.net.http.rest.api.endpoint

import bvanseg.kotlincommons.io.net.http.rest.RestActionImpl
import bvanseg.kotlincommons.io.net.http.rest.api.request.GetRequest

/**
 * @author Boston Vanseghi
 * @since 2.11.0
 */
interface GetEndpoint<T> {
    fun get(restRequest: GetRequest): RestActionImpl<T>
}