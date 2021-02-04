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
import bvanseg.kotlincommons.time.api.Khrono
import bvanseg.kotlincommons.time.api.milliseconds
import bvanseg.kotlincommons.time.api.performer.every
import bvanseg.kotlincommons.util.event.EventBus
import bvanseg.kotlincommons.util.ratelimit.event.BucketRefillEvent
import bvanseg.kotlincommons.util.ratelimit.event.RateLimiterShutdownEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author Boston Vanseghi
 * @author Jacob Glickman (https://github.com/jhg023)[https://github.com/jhg023]
 * @since 2.3.4
 */
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
        val initDelta = calculateSleepTime(false)

        every(tokenBucket.refillTime, counterDrift = true) { performer ->
            val preRefillEvent = BucketRefillEvent.PRE(this@RateLimiter)
            val postRefillEvent = BucketRefillEvent.POST(this@RateLimiter)
            eventBus.fire(preRefillEvent)
            tokenBucket.refill()
            eventBus.fire(postRefillEvent)

            if (!isRunning.get()) {
                performer.stop()
            }
        }.delay(initDelta).offset(tokenBucket.refillTimeOffset).execute(async = true)

        // Used only for asynchronous tasks.
        GlobalScope.launch(Dispatchers.IO) {
            while (rateLimiter.isRunning.get()) {
                try {
                    val next = rateLimiter.asyncDeque.takeFirst() ?: continue
                    val cost = next.first

                    if (tokenBucket.tryConsume(cost)) {
                        val callback = next.second
                        logger.trace(
                            "Executing queued submission: TokenBucket ({}/{}).",
                            tokenBucket.currentTokenCount,
                            tokenBucket.tokenLimit
                        )
                        GlobalScope.launch {
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
                        }
                    } else {
                        rateLimiter.asyncDeque.offerFirst(next)

                        EventBus.DEFAULT.awaitCoroutineEvent<BucketRefillEvent.POST>()
                    }
                } catch (e: Exception) {
                    exceptionStrategy(e)
                }
            }
        }
    }

    var shutdownStrategy: () -> Unit = {
        asyncDeque.clear()
    }

    /**
     * Collects asynchronous tasks paired up with their token cost.
     */
    private val asyncDeque = LinkedBlockingDeque<Pair<Long, () -> Unit>>()

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
     * Calculates the time to sleep until the next refill interval.
     *
     * @param flag True or false if the calculated sleep time should include the refill time offset.
     *
     * @return A [Khrono] of the amount of time to sleep as milliseconds.
     */
    fun calculateSleepTime(flag: Boolean): Khrono = tokenBucket.refillTime.nextInterval().toMillis() +
            (if (flag) tokenBucket.refillTimeOffset.toMillis() else Khrono.EMPTY)

    /**
     * Submits an asynchronous task to the [RateLimiter] to be executed once a token is readily available.
     *
     * @param consume The number of tokens the action consumes. Defaults to 1 token.
     * @param ratelimitCallback - The callback to execute once a token is available.
     */
    fun submit(consume: Long = 1, ratelimitCallback: () -> Unit) {
        if (!isConsumeValid(consume)) return

        logger.trace("Received asynchronous submission.")
        asyncDeque.addLast(consume to ratelimitCallback)
    }

    /**
     * Submits a blocking task to the [RateLimiter] to be executed once a token is readily available.
     *
     * @param consume The amount of tokens to consume for the given task. Defaults to 1.
     * @param callback The callback to execute once a token is readily available.
     *
     * @return The result returned from the invoked [callback].
     */
    fun <R> submitBlocking(consume: Long = 1, callback: () -> R): R {
        submitBlocking(consume)
        return callback()
    }

    /**
     * Submits a blocking task to the [RateLimiter] to be executed once a token is readily available.
     *
     * @param consume The amount of tokens to consume for the given task. Defaults to 1.
     */
    fun submitBlocking(consume: Long = 1) {
        while (true) {
            if (tokenBucket.tryConsume(consume)) {
                break
            } else {
                EventBus.DEFAULT.awaitThreadEvent<BucketRefillEvent.POST>()
            }
        }
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
     * Starts the [RateLimiter]. If the [RateLimiter] is already running, the function will return prematurely.
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
}