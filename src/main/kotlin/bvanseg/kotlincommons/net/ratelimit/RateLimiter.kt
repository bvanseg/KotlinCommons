package bvanseg.kotlincommons.net.ratelimit

import bvanseg.kotlincommons.any.getLogger
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicLong

/**
 * @author Boston Vanseghi
 * @author Jacob Glickman (https://github.com/jhg023)[https://github.com/jhg023]
 * @since 2.3.4
 */
class RateLimiter<T>(val tokenBucket: TokenBucket, private val service: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor {
    val thread = Thread(it)
    thread.isDaemon = true
    thread
},
cycleStrategy: (RateLimiter<T>, AtomicLong, ConcurrentLinkedDeque<Pair<Long, () -> Unit>>) -> Unit = { ratelimiter, bc, q ->
    service.scheduleAtFixedRate({
        tokenBucket.refill()

        while (tokenBucket.isNotEmpty()) {
            while (bc.get() > 0) { Thread.onSpinWait() }

            val next = q.pollFirst()

            next?.let {
                val pair = tokenBucket.tryConsume(it.first, it.second)

                if (pair.first) {
                    ratelimiter.logger.trace("Executed queued submission: TokenBucket (${tokenBucket.currentTokenCount}/${tokenBucket.tokenLimit}).")
                } else {
                    q.addFirst(it.first to it.second)
                }
            } ?: break
        }
    }, tokenBucket.refillTime, tokenBucket.refillTime, TimeUnit.MILLISECONDS)
}) {

    val logger = getLogger()
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
        blockingCount.incrementAndGet()

        logger.trace("Executing blocking submission...")

        while (true) {
            val pair = tokenBucket.tryConsume(consume, callback)

            if (pair.first) {
                logger.trace("Finished executing submission: TokenBucket " +
                        "(${tokenBucket.currentTokenCount}/${tokenBucket.tokenLimit}).")
                blockingCount.decrementAndGet()
                return pair.second!!
            }
        }
    }

    fun submitBlocking(consume: Long = 1) {
        blockingCount.incrementAndGet()

        logger.trace("Entering blocking submission...")
        while(!(tokenBucket.tryConsume(consume) {}).first) { Thread.onSpinWait() }

        logger.trace("Leaving blocking submission: TokenBucket (${tokenBucket.currentTokenCount}/${tokenBucket.tokenLimit}).")
        blockingCount.decrementAndGet()
    }

    fun shutdown() {
        logger.trace("Shutting down RateLimiter...")
        service.shutdown()
    }
}