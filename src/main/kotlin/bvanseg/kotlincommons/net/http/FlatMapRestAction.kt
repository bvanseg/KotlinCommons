/*
 * MIT License
 *
 * Copyright (c) 2021 Boston Vanseghi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package bvanseg.kotlincommons.net.http

/**
 * An expansion of [RestAction] that allows for chaining [RestAction] queues.
 *
 * @author Boston Vanseghi
 * @since 2.3.0
 */
class FlatMapRestAction<T, O>(val callback: (T?) -> RestAction<O>, private val parent: RestAction<T>): RestAction<O>() {

    override fun queueImpl(): FlatMapRestAction<T, O> {
        parent.queue {
            callback(it)
        }

        return this
    }

    override fun queueImpl(callback: (O) -> Unit): FlatMapRestAction<T, O> {
        parent.queue {
            val resultAction = this.callback(it)
            resultAction.queue { result ->
                callback(result)
            }
        }

        return this
    }

    override fun completeImpl(): O? {
        val value = parent.complete()
        return callback(value).complete()
    }
}