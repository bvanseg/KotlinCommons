package bvanseg.kotlincommons.io.net.http.rest.endpoint

import bvanseg.kotlincommons.io.net.http.rest.RestActionImpl
import bvanseg.kotlincommons.io.net.http.rest.request.DeleteRequest

/**
 * @author Boston Vanseghi
 * @since 2.11.0
 */
interface DeleteEndpoint<T> {
    fun delete(restRequest: DeleteRequest): RestActionImpl<T>
}