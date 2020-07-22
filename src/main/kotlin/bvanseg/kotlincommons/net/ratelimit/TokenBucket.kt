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
    private val initTokenCount: Long = tokenLimit,
    private val refreshStrategy: (TokenBucket) -> Unit = {
        it.currentTokenCount = tokenLimit
    }
) {
    @Volatile
    var lastUpdate = initUpdate
        private set

    @Volatile
    var currentTokenCount: Long = initTokenCount

    fun refill() = synchronized(this) {
        refreshStrategy(this)
        lastUpdate = System.currentTimeMillis()
    }

    fun isFull() = currentTokenCount
    fun isNotFull() = currentTokenCount != tokenLimit
    fun isEmpty() = currentTokenCount == 0L
    fun isNotEmpty() = currentTokenCount > 0L

    fun tryConsume(): Boolean = synchronized(this) {
        if(isNotEmpty()) {
            currentTokenCount--
            return true
        }
        return false
    }
}