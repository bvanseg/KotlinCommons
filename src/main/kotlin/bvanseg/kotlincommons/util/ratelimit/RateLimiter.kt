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
package bvanseg.kotlincommons.util.ratelimit

import bvanseg.kotlincommons.io.logging.getLogger
import bvanseg.kotlincommons.util.event.EventBus
import bvanseg.kotlincommons.util.project.Experimental
import bvanseg.kotlincommons.util.ratelimit.event.BucketEmptyEvent
import bvanseg.kotlincommons.util.ratelimit.event.BucketRefillEvent
import bvanseg.kotlincommons.util.ratelimit.event.RateLimiterShutdownEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Instant
import java.time.OffsetDateTime
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.atomic.AtomicLong

/**
 * @author Boston Vanseghi
 * @author Jacob Glickman (https://github.com/jhg023)[https://github.com/jhg023]
 * @since 2.3.4
 */
@Experimental
class RateLimiter<T> constructor(
    val tokenBucket: TokenBucket,
    val eventBus: EventBus = EventBus.DEFAULT,
    autoStart: Boolean = true
) {

    companion object {
        val logger = getLogger()
    }

    var cycleStrategy: (RateLimiter<T>) -> Unit = { rateLimiter ->
        GlobalScope.launch(Dispatchers.IO) {
            var nextRefreshTime = 0L
            while (rateLimiter.isRunning) {

                val preRefillEvent = BucketRefillEvent.PRE(this@RateLimiter)
                val postRefillEvent = BucketRefillEvent.POST(this@RateLimiter)
                eventBus.fire(preRefillEvent)
                tokenBucket.refill()
                eventBus.fire(postRefillEvent)

                while (tokenBucket.isNotEmpty()) {
                    val next = rateLimiter.submissionTypeDeque.takeFirst() ?: continue

                    if (System.currentTimeMillis() >= nextRefreshTime) {
                        eventBus.fire(preRefillEvent)
                        tokenBucket.refill()
                        eventBus.fire(postRefillEvent)
                    }

                    when (next.first) {
                        SubmissionType.SYNCHRONOUS -> {
                            val cost = next.second
                            if (tokenBucket.tryConsume(cost)) {
                                logger.trace(
                                    "Executing blocking submission: TokenBucket ({}/{}).",
                                    tokenBucket.currentTokenCount,
                                    tokenBucket.tokenLimit
                                )
                                rateLimiter.syncChannel.send(rateLimiter.finishedSyncID.getAndIncrement())
                            } else {
                                rateLimiter.submissionTypeDeque.offerFirst(next)
                            }
                        }
                        SubmissionType.ASYNCHRONOUS -> {
                            val cost = next.second

                            if (tokenBucket.tryConsume(cost)) {
                                val callback = rateLimiter.asyncDeque.poll()
                                logger.trace(
                                    "Executing queued submission: TokenBucket ({}/{}).",
                                    tokenBucket.currentTokenCount,
                                    tokenBucket.tokenLimit
                                )
                                callback()
                                logger.trace(
                                    "Finished executing queued submission: TokenBucket ({}/{}).",
                                    tokenBucket.currentTokenCount,
                                    tokenBucket.tokenLimit
                                )
                            } else {
                                rateLimiter.submissionTypeDeque.offerFirst(next)
                            }
                        }
                    }
                }

                val bucketEmptyEvent = BucketEmptyEvent(this@RateLimiter)
                eventBus.fire(bucketEmptyEvent)

                val snapshotMillis = Instant.now().toEpochMilli() + OffsetDateTime.now().offset.totalSeconds * 1000L
                // Get the delta between the next interval and the current time.
                val delta = tokenBucket.refillTime - snapshotMillis % tokenBucket.refillTime

                nextRefreshTime = System.currentTimeMillis() + delta

                delay(delta)
            }
        }
    }

    var shutdownStrategy: () -> Unit = { }

    /**
     * The channel to send synchronous IDs through.
     */
    private val syncChannel = Channel<Long>()

    /**
     * The latest ID of the synchronous tasks.
     */
    private val workingSyncID = AtomicLong()

    /**
     * The current number of synchronous tasks finished so far.
     */
    private val finishedSyncID = AtomicLong()

    /**
     * The number of queued up submissions awaiting execution by the [RateLimiter].
     */
    private val asyncDeque = ConcurrentLinkedDeque<() -> Unit>()

    /**
     * Maintains the order of submission types to their consumption cost.
     */
    private val submissionTypeDeque = LinkedBlockingDeque<Pair<SubmissionType, Long>>()

    /**
     *
     */
    var isRunning: Boolean = true
        private set

    init {
        if (autoStart) {
            start()
        }
    }

    /**
     * Submits an asynchronous task to the [RateLimiter] to be executed once a token is readily available.
     *
     * @param consume The number of tokens the action consumes. Defaults to 1 token.
     * @param ratelimitCallback - The callback to execute once a token is available.
     */
    fun submit(consume: Long = 1, ratelimitCallback: () -> Unit) {
        logger.trace("Received asynchronous submission.")
        submissionTypeDeque.offerFirst(SubmissionType.ASYNCHRONOUS to consume)
        asyncDeque.addLast(ratelimitCallback)
    }

    /**
     * Submits a blocking task to the [RateLimiter] to be executed once a token is readily available.
     *
     * @param consume The amount of tokens to consume for the given task. Defaults to 1.
     * @param callback The callback to execute once a token is readily available.
     */
    fun <R> submitBlocking(consume: Long = 1, callback: () -> R): R {
        submitBlocking(consume)
        return callback()
    }

    /**
     * Blocks the current thread until tokens of the specified amount are consumed.
     *
     * @param consume The amount of tokens to consume for the given task. Defaults to 1.
     */
    fun submitBlocking(consume: Long = 1) = runBlocking {
        submissionTypeDeque.offerFirst(SubmissionType.SYNCHRONOUS to consume)

        val uniqueID = workingSyncID.getAndIncrement()

        while (true) {
            val id = syncChannel.receive()

            if (uniqueID == id) {
                break
            }
        }
    }

    fun start() {
        logger.trace("Starting RateLimiter...")
        cycleStrategy(this)
    }

    /**
     * Shuts down the [RateLimiter]'s internal scheduler.
     */
    fun shutdown() {
        val preShutdownEvent = RateLimiterShutdownEvent.PRE(this)
        val postShutdownEvent = RateLimiterShutdownEvent.POST(this)
        logger.trace("Shutting down RateLimiter...")
        eventBus.fire(preShutdownEvent)
        isRunning = false
        shutdownStrategy()
        eventBus.fire(postShutdownEvent)
    }
}