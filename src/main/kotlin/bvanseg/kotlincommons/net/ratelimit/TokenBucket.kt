package bvanseg.kotlincommons.net.ratelimit

import bvanseg.kotlincommons.any.getLogger
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.ReentrantLock

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
    private val initTokenCount: Long = tokenLimit,
    private val refreshStrategy: (TokenBucket) -> Unit = {
        it.currentTokenCount = tokenLimit
    }
) {
    @Volatile
    var lastUpdate = initUpdate
        private set

    private val logger = getLogger()

    private val lock = ReentrantLock(true)

    @Volatile
    var currentTokenCount: Long = initTokenCount

    fun refill() {
        lock.lock()
        logger.trace("Refreshing tokens: TokenBucket ($currentTokenCount/$tokenLimit).")
        refreshStrategy(this)
        logger.trace("Finished refreshing tokens: TokenBucket ($currentTokenCount/$tokenLimit).")
        lastUpdate = System.currentTimeMillis()
        lock.unlock()
    }

    fun isFull() = currentTokenCount
    fun isNotFull() = currentTokenCount != tokenLimit
    fun isEmpty() = currentTokenCount == 0L
    fun isNotEmpty() = currentTokenCount > 0L

    fun <R> tryConsume(amount: Long = 1, callback: () -> R): Pair<Boolean, R?> {
        lock.lock()
        if (currentTokenCount >= amount) {
            currentTokenCount -= amount
            try {
                return true to callback()
            } finally {
            	lock.unlock()
            }
        }

        try {
            return false to null
        } finally {
        	lock.unlock()
        }
    }
}