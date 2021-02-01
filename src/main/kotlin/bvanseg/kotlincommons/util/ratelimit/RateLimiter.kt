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

import bvanseg.kotlincommons.io.logging.debug
import bvanseg.kotlincommons.io.logging.getLogger
import bvanseg.kotlincommons.io.logging.trace
import bvanseg.kotlincommons.util.any.sync
import bvanseg.kotlincommons.util.event.EventBus
import bvanseg.kotlincommons.util.ratelimit.event.BucketEmptyEvent
import bvanseg.kotlincommons.util.ratelimit.event.BucketRefillEvent
import bvanseg.kotlincommons.util.ratelimit.event.RateLimiterShutdownEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong

/**
 * @author Boston Vanseghi
 * @author Jacob Glickman (https://github.com/jhg023)[https://github.com/jhg023]
 * @since 2.3.4
 */
@Suppress("EXPERIMENTAL_API_USAGE")
class RateLimiter constructor(
    val tokenBucket: TokenBucket,
    val eventBus: EventBus = EventBus.DEFAULT,
    autoStart: Boolean = true
) {

    companion object {
        val logger = getLogger()
    }

    var exceptionStrategy: (Throwable) -> Unit = {
        it.printStackTrace()
    }

    var cycleStrategy: (RateLimiter) -> Unit = { rateLimiter ->
        GlobalScope.launch(Dispatchers.IO) {

            var nextRefreshTime = 0L

            while (rateLimiter.isRunning.get()) {

                while (true) {
                    val next = rateLimiter.submissionTypeDeque.takeFirst() ?: continue

                    if (System.currentTimeMillis() >= nextRefreshTime + tokenBucket.refillTimeOffset) {
                        try {
                            val preRefillEvent = BucketRefillEvent.PRE(this@RateLimiter)
                            val postRefillEvent = BucketRefillEvent.POST(this@RateLimiter)
                            eventBus.fire(preRefillEvent)
                            tokenBucket.refill()
                            eventBus.fire(postRefillEvent)

                            // Reset the next refresh time.
                            val snapshotMillis = System.currentTimeMillis() % tokenBucket.refillTime
                            val delta = tokenBucket.refillTime - snapshotMillis
                            nextRefreshTime = System.currentTimeMillis() + delta
                        } catch (e: Exception) {
                            exceptionStrategy(e)
                        }
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

                                if (rateLimiter.syncChannel.isClosedForSend) {
                                    logger.warn("Synchronous send channel is closed, receiver channels may be dead. Attempting recovery...")
                                    attemptChannelRecovery()
                                }

                                rateLimiter.syncChannel.send(rateLimiter.finishedSyncID.get())
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
                                try {
                                    callback()
                                    logger.trace(
                                        "Finished executing queued submission: TokenBucket ({}/{}).",
                                        tokenBucket.currentTokenCount,
                                        tokenBucket.tokenLimit
                                    )
                                } catch (e: Exception) {
                                    exceptionStrategy(e)
                                }
                            } else {
                                rateLimiter.submissionTypeDeque.offerFirst(next)
                            }
                        }
                    }

                    if (tokenBucket.isEmpty()) {
                        break
                    }
                }

                try {
                    val bucketEmptyEvent = BucketEmptyEvent(this@RateLimiter)
                    eventBus.fire(bucketEmptyEvent)
                } catch (e: Exception) {
                    exceptionStrategy(e)
                }

                val sleepTime = (nextRefreshTime - System.currentTimeMillis()) + tokenBucket.refillTimeOffset
                logger.debug { "Sleeping for $sleepTime milliseconds..." }
                delay(sleepTime)
            }
        }
    }

    var shutdownStrategy: () -> Unit = {
        // Cancel receiver channels and clear map.
        receiverChannels.forEach { (_, channel) ->
            channel.cancel()
        }
        receiverChannels.clear()

        // Cancel primary sync channel.
        syncChannel.cancel()

        // Clear Deques
        asyncDeque.clear()
        submissionTypeDeque.clear()

        // Reset sync IDs.
        workingSyncID.set(0L)
        finishedSyncID.set(0L)
    }

    /**
     * The channel to send synchronous IDs through.
     */
    private var syncChannel = BroadcastChannel<Long>(Channel.BUFFERED)

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

    private val receiverChannels = ConcurrentHashMap<Long, ReceiveChannel<Long>>()

    /**
     * Indicates whether or not the [RateLimiter]'s cycle strategy is executing periodically.
     */
    val isRunning: AtomicBoolean = AtomicBoolean()

    init {
        if (autoStart) {
            start()
        }
    }

    /**
     * Attempts to recover the synchronous channels to a working state.
     */
    private fun attemptChannelRecovery() {
        logger.debug("Attempting to recover RateLimiter channels...")
        // Close the current sync channel if open and create a new one.
        syncChannel.cancel()
        syncChannel = BroadcastChannel(Channel.BUFFERED)

        // Close all existing receiver channels if necessary and regenerate them.
        receiverChannels.sync {
            // Cancel all channels in case they are open.
            receiverChannels.forEach { (_, channel) ->
                channel.cancel()
            }
            // For all channel keys, generate a new subscription for them.
            receiverChannels.keys.forEach { key ->
                receiverChannels[key] = syncChannel.openSubscription()
            }
        }
        logger.debug("Finished recovering RateLimiter channels.")
    }

    /**
     * Submits an asynchronous task to the [RateLimiter] to be executed once a token is readily available.
     *
     * @param consume The number of tokens the action consumes. Defaults to 1 token.
     * @param ratelimitCallback - The callback to execute once a token is available.
     */
    fun submit(consume: Long = 1, ratelimitCallback: () -> Unit) {
        if (!isConsumeValid(consume)) return

        logger.trace("Received asynchronous submission.")
        asyncDeque.addLast(ratelimitCallback)
        submissionTypeDeque.offerLast(SubmissionType.ASYNCHRONOUS to consume)
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
        if (!isConsumeValid(consume)) return@runBlocking

        var receiver = getSyncReceiverChannel()

        submissionTypeDeque.offerLast(SubmissionType.SYNCHRONOUS to consume)

        val uniqueID = workingSyncID.getAndIncrement()

        while (true) {
            if (receiver.isClosedForReceive) {
                receiver = getSyncReceiverChannel()
            }

            val id = receiver.receive()

            if (uniqueID == id) {
                break
            }
        }

        finishedSyncID.incrementAndGet()
    }

    /**
     * Validates whether or not the given [consume] amount is viable.
     *
     * @param consume The consume amount trying to be used.
     *
     * @return true or false if the consume count is good to use.
     */
    private fun isConsumeValid(consume: Long): Boolean = when {
        consume < 0 -> {
            logger.warn("Consume count for rate limiter submission can not be negative: {}", consume)
            false
        }
        consume > tokenBucket.tokenLimit -> {
            logger.warn(
                "Consume count for rate limiter submission can not be greater than token limit: {}/{}",
                consume,
                tokenBucket.tokenLimit
            )
            false
        }
        else -> true
    }

    /**
     * Starts the [RateLimiter].
     */
    fun start() {
        if (isRunning.getAndSet(true)) {
            logger.warn("Attempted to start RateLimiter but it is already running!")
            return
        }
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
        isRunning.set(false)
        shutdownStrategy()
        eventBus.fire(postShutdownEvent)
    }

    /**
     * @author Boston Vanseghi
     * @since 2.7.0
     */
    fun getSyncReceiverChannel(): ReceiveChannel<Long> {
        val threadID = Thread.currentThread().id

        return receiverChannels.compute(threadID) { _, channel ->
            var toReturn = channel
            if (toReturn == null || toReturn.isClosedForReceive) {
                logger.debug("Creating new receiver channel for Rate Limiter...")
                toReturn = syncChannel.openSubscription()
            }

            toReturn
        }!!
    }
}