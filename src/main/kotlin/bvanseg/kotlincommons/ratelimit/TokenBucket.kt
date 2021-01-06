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

    companion object {
        private val logger = getLogger()
    }

    private val lock = ReentrantLock(true)

    @Volatile
    var currentTokenCount: Long = initTokenCount

    fun refill() {
        if (currentTokenCount < tokenLimit) {
            try {
                lock.lock()
                logger.debug("Refreshing tokens: TokenBucket ($currentTokenCount/$tokenLimit).")
                refreshStrategy(this)
                logger.debug("Finished refreshing tokens: TokenBucket ($currentTokenCount/$tokenLimit).")
                lastUpdate = System.currentTimeMillis()
            } finally {
                lock.unlock()
            }
        }
    }

    fun isFull() = currentTokenCount
    fun isNotFull() = currentTokenCount != tokenLimit
    fun isEmpty() = currentTokenCount == 0L
    fun isNotEmpty() = currentTokenCount > 0L

    fun <R> tryConsume(amount: Long = 1, callback: () -> R?): Pair<Boolean, R?> {
        if (amount < 0) {
            throw IllegalArgumentException("Consume amount may not be negative!")
        }

        try {
            lock.lock()
            if (currentTokenCount >= amount) {
                currentTokenCount -= amount
                return true to callback()
            }

            return false to null

        } finally {
            lock.unlock()
        }
    }
}