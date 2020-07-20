package bvanseg.kotlincommons.net.ratelimit

import bvanseg.kotlincommons.any.getLogger
import java.time.Instant
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

/**
 * @author Boston Vanseghi
 * @author Jacob Glickman (https://github.com/jhg023)[https://github.com/jhg023]
 * @since 2.3.4
 */
class RateLimiter<T>(val tokenBucket: TokenBucket) {

    val logger = getLogger()
    private val blockingCount = AtomicLong(0)
    private val queue = ConcurrentLinkedDeque<Pair<T, (T) -> Unit>>()
    private val service = Executors.newSingleThreadScheduledExecutor {
        val thread = Thread(it)
        thread.isDaemon = true
        thread
    }

    init {
        service.scheduleAtFixedRate({
            tokenBucket.refill()

            while(tokenBucket.isNotEmpty()) {
                while(blockingCount.get() > 0) { Thread.onSpinWait() }
                val next = queue.pollFirst()
                next?.let {
                    val result = tokenBucket.tryConsume()
                    if(result) {
                        it.second(it.first)
                        logger.trace("Executing queued submission: TokenBucket (${tokenBucket.currentTokenCount.get()}/${tokenBucket.tokenLimit}).")
                    } else
                        queue.addFirst(it.first to it.second)
                } ?: break
            }

        }, tokenBucket.refillTime, tokenBucket.refillTime, TimeUnit.MILLISECONDS)
    }

    /**
     * Submits a unit to the rate limiter, along with a success callback to run it with as it passes
     * the rate limit.
     *
     * @param unit - The unit to be submitted.
     * @param ratelimitCallback - The callback to run on [unit] once it has a token.
     */
    fun submit(unit: T, ratelimitCallback: (T) -> Unit) {
        logger.trace("Received asynchronous submission.")
        if(queue.isEmpty()) {
            val result = tokenBucket.tryConsume()

            if(result) {
                logger.trace("Immediately executing asynchronous submission: TokenBucket (${tokenBucket.currentTokenCount.get()}/${tokenBucket.tokenLimit}).")
                ratelimitCallback(unit)
            }
            else
                queue.addLast(unit to ratelimitCallback)
        }
        else if(queue.size < tokenBucket.maxSize) {
            queue.addLast(unit to ratelimitCallback)
        }
    }

    fun <R> submitBlocking(unit: T, callback: (T) -> R): R {
        blockingCount.incrementAndGet()

        logger.trace("Received blocking submission.")
        while(!tokenBucket.tryConsume()) { Thread.onSpinWait() }

        logger.trace("Executing blocking submission...")
        val callbackResult = callback(unit)
        blockingCount.decrementAndGet()
        return callbackResult
    }

    fun shutdown() {
        logger.trace("Shutting down RateLimiter...")
        service.shutdown()
    }
}