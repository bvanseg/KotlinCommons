package bvanseg.kotlincommons.io.net.http.rest.endpoint

import bvanseg.kotlincommons.io.net.http.rest.RestActionImpl
import bvanseg.kotlincommons.io.net.http.rest.request.PatchRequest

/**
 * @author Boston Vanseghi
 * @since 2.11.0
 */
interface PatchEndpoint<T> {
    fun patch(restRequest: PatchRequest): RestActionImpl<T>
}