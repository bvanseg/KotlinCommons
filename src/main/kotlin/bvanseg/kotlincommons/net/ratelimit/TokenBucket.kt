package bvanseg.kotlincommons.net.ratelimit

import java.util.concurrent.atomic.AtomicLong

/**
 * A simple implementation of a token bucket.
 *
 * @author Boston Vanseghi
 * @author Jacob Glickman (https://github.com/jhg023)[https://github.com/jhg023]
 * @since 2.3.4
 */
data class TokenBucket(
    val tokenLimit: Long,
    val maxSize: Long,
    val refillTime: Long,
    private val initUpdate: Long,
    private val initTokenCount: Long = tokenLimit
) {
    var lastUpdate = AtomicLong(initUpdate)
    var currentTokenCount: AtomicLong = AtomicLong(initTokenCount)

    fun refill() {
        currentTokenCount.set(tokenLimit)
        lastUpdate.set(System.currentTimeMillis())
    }

    fun isFull() = currentTokenCount.get() == tokenLimit
    fun isNotFull() = currentTokenCount.get() != tokenLimit
    fun isEmpty() = currentTokenCount.get() == 0L
    fun isNotEmpty() = currentTokenCount.get() > 0L

    fun tryConsume() = currentTokenCount.getAndDecrement() > 0
}