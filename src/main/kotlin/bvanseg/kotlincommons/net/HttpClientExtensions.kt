package bvanseg.kotlincommons.net

import java.net.http.HttpRequest

fun HttpRequest.Builder.PATCH(publisher: HttpRequest.BodyPublisher) = this.method("PATCH", publisher)

fun httpRequest(block: KCHttpRequest.() -> Unit): HttpRequest {
    val kcRequest = KCHttpRequest()
    kcRequest.block()
    return kcRequest.requestBuilder.build()
}

