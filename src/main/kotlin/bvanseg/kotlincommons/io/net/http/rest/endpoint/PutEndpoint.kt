package bvanseg.kotlincommons.io.net.http.rest.endpoint

import bvanseg.kotlincommons.io.net.http.rest.RestActionImpl
import bvanseg.kotlincommons.io.net.http.rest.request.PutRequest

/**
 * @author Boston Vanseghi
 * @since 2.11.0
 */
interface PutEndpoint<T> {
    fun put(restRequest: PutRequest): RestActionImpl<T>
}