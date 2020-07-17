package bvanseg.kotlincommons.net

import java.net.http.HttpRequest

@Deprecated("Header blocks can not be nested within themselves!", level = DeprecationLevel.ERROR,
    replaceWith = ReplaceWith("error(\"...\")")
)
fun HashMap<*,*>.headers(block: HashMap<String, String>.() -> Unit): Nothing = error("...")

fun HttpRequest.Builder.PATCH(publisher: HttpRequest.BodyPublisher) = this.method("PATCH", publisher)

fun httpRequest(block: KCHttpRequest.() -> Unit): HttpRequest {
    val kcRequest = KCHttpRequest()
    kcRequest.block()
    return kcRequest.requestBuilder.build()
}

