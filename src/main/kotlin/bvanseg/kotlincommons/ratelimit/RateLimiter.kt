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
package bvanseg.kotlincommons.ratelimit

import bvanseg.kotlincommons.any.getLogger
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicLong

/**
 * @author Boston Vanseghi
 * @author Jacob Glickman (https://github.com/jhg023)[https://github.com/jhg023]
 * @since 2.3.4
 */
class RateLimiter<T>(
    val tokenBucket: TokenBucket,
    private val service: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor {
        val thread = Thread(it)
        thread.isDaemon = true
        thread
    },
    cycleStrategy: (RateLimiter<T>, AtomicLong, ConcurrentLinkedDeque<Pair<Long, () -> Unit>>) -> Unit = { ratelimiter, bc, q ->
        service.scheduleAtFixedRate({
            tokenBucket.refill()

            while (tokenBucket.isNotEmpty()) {
                while (bc.get() > 0) {
                    Thread.onSpinWait()
                }

                val next = q.pollFirst()

                next?.let {
                    val pair = tokenBucket.tryConsume(it.first, it.second)

                    if (pair.first) {
                        logger.trace(
                            "Executed queued submission: TokenBucket ({}/{}).",
                            tokenBucket.currentTokenCount,
                            tokenBucket.tokenLimit
                        )
                    } else {
                        q.addFirst(it.first to it.second)
                    }
                } ?: break
            }
        }, tokenBucket.refillTime, tokenBucket.refillTime, TimeUnit.MILLISECONDS)
    }
) {

    companion object {
        val logger = getLogger()
    }

    private val blockingCount = AtomicLong(0)
    private val queue = ConcurrentLinkedDeque<Pair<Long, () -> Unit>>()

    init {
        cycleStrategy(this, blockingCount, queue)
    }

    /**
     * Submits a unit to the rate limiter, along with a success callback to run it with as it passes
     * the rate limit.
     *
     * @param unit - The unit to be submitted.
     * @param ratelimitCallback - The callback to run on [unit] once it has a token.
     */
    fun submit(consume: Long = 1, ratelimitCallback: () -> Unit) {
        logger.trace("Received asynchronous submission.")

        if (queue.isEmpty()) {
            synchronized(tokenBucket) {
                val pair = tokenBucket.tryConsume(consume, ratelimitCallback)

                if (!pair.first) {
                    queue.addLast(consume to ratelimitCallback)
                }
            }
        } else if (queue.size < tokenBucket.maxSize) {
            queue.addLast(consume to ratelimitCallback)
        }
    }

    fun <R> submitBlocking(consume: Long = 1, callback: () -> R): R {

        logger.trace("Executing blocking submission...")

        while (true) {
            blockingCount.incrementAndGet()
            val pair = tokenBucket.tryConsume(consume, callback)
            blockingCount.decrementAndGet()

            if (pair.first) {
                logger.trace(
                    "Finished executing submission: TokenBucket " +
                            "({}/{}).", tokenBucket.currentTokenCount, tokenBucket.tokenLimit
                )
                return pair.second!!
            }
        }
    }

    fun submitBlocking(consume: Long = 1) {

        logger.trace("Entering blocking submission...")
        while (true) {
            blockingCount.incrementAndGet()
            val pair = tokenBucket.tryConsume(consume) {}
            blockingCount.decrementAndGet()

            if (pair.first) {
                logger.trace(
                    "Finished executing submission: TokenBucket " +
                            "({}/{}).", tokenBucket.currentTokenCount, tokenBucket.tokenLimit
                )
                break
            }
        }
    }

    fun shutdown() {
        logger.trace("Shutting down RateLimiter...")
        service.shutdown()
    }
}