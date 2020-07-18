package bvanseg.kotlincommons.net.rest

/**
 * An expansion of [RestAction] that allows for chaining [RestAction] queues.
 *
 * @author Boston Vanseghi
 * @since 2.3.0
 */
class FlatMapRestAction<T, O>(val callback: (T?) -> RestAction<O>, private val parent: RestAction<T>) : RestAction<O>() {

    override fun queueImpl() {
        parent.queue {
            callback(it)
        }
    }

    override fun queueImpl(callback: (O) -> Unit) {
        parent.queue {
            val resultAction = this.callback(it)
            resultAction.queue { result ->
                callback(result)
            }
        }
    }

    override fun completeImpl(): O {
        val value = parent.complete()
        return callback(value).complete()
    }
}