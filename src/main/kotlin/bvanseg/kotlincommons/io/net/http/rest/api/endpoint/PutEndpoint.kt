package bvanseg.kotlincommons.io.net.http.rest.api.endpoint

import bvanseg.kotlincommons.io.net.http.rest.RestActionImpl
import bvanseg.kotlincommons.io.net.http.rest.api.request.PutRequest

/**
 * @author Boston Vanseghi
 * @since 2.11.0
 */
interface PutEndpoint<T> {
    fun put(restRequest: PutRequest): RestActionImpl<T>
}