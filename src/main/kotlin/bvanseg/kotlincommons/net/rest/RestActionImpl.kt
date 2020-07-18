package bvanseg.kotlincommons.net.rest

import java.net.http.HttpRequest
import java.net.http.HttpResponse

class RestActionImpl<T>(private val request: HttpRequest, val type: Class<T>): RestAction<T>() {

    override fun queueImpl() {
        KCHttp.DEFAULT_HTTP_CLIENT.sendAsync(request, HttpResponse.BodyHandlers.discarding()).thenAcceptAsync {
            successCallback?.invoke(it)
        }
    }

    override fun queueImpl(callback: (T) -> Unit) {
        KCHttp.DEFAULT_HTTP_CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAcceptAsync { response ->
            try {
                if (response.body().isNotEmpty() && type != HttpResponse::class.java && type != String::class.java)
                    callback(KCHttp.jsonMapper.readValue(response.body(), type))
                else if(type == HttpResponse::class.java)
                    callback(response as T)
                else if(type == String::class.java)
                    callback(response.body() as T)

                successCallback?.invoke(response)
            } catch (e: Exception) {
                e.printStackTrace()
                exceptionCallback?.invoke(e)
            }
        }
    }

    override fun completeImpl(): T {
        val response = KCHttp.DEFAULT_HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString())

        return if (response.body().isNotEmpty())
            KCHttp.jsonMapper.readValue(response.body(), type)
        else
            response as T
    }
}