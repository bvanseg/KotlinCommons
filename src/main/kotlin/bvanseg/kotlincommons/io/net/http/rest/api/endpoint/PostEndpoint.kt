package bvanseg.kotlincommons.io.net.http.rest.api.endpoint

import bvanseg.kotlincommons.io.net.http.rest.RestActionImpl
import bvanseg.kotlincommons.io.net.http.rest.api.request.PostRequest

/**
 * @author Boston Vanseghi
 * @since 2.11.0
 */
interface PostEndpoint<T> {
    fun post(restRequest: PostRequest): RestActionImpl<T>
}