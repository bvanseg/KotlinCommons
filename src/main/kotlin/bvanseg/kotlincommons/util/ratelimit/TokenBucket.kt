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
import bvanseg.kotlincommons.io.logging.trace
import bvanseg.kotlincommons.lang.checks.Checks
import bvanseg.kotlincommons.time.api.Khrono
import bvanseg.kotlincommons.util.HashCodeBuilder
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
    val refillTime: Khrono,
    val refillTimeOffset: Khrono = Khrono.EMPTY,
    private val initUpdate: Long = System.currentTimeMillis(),
    var currentTokenCount: AtomicLong = AtomicLong(tokenLimit),
    private val refillStrategy: (TokenBucket) -> Unit = {
        it.currentTokenCount.set(tokenLimit)
    }
) {
    @Volatile
    var lastUpdate = initUpdate
        private set

    companion object {
        private val logger = getLogger()
    }

    private val lock = ReentrantLock(true)

    fun refill() {
        try {
            logger.trace { "Preparing to enter lock in TokenBucket#refill... " }
            lock.lock()
            logger.debug("Refilling tokens: TokenBucket ({}/{}).", currentTokenCount, tokenLimit)
            refillStrategy(this)
            logger.debug("Finished refilling tokens: TokenBucket ({}/{}).", currentTokenCount, tokenLimit)
            lastUpdate = System.currentTimeMillis()
        } finally {
            lock.unlock()
            logger.trace { "Successfully left lock in TokenBucket#refill!" }
        }
    }

    fun isFull(): Boolean = currentTokenCount.get() == tokenLimit
    fun isNotFull(): Boolean = currentTokenCount.get() != tokenLimit
    fun isEmpty(): Boolean = currentTokenCount.get() == 0L
    fun isNotEmpty(): Boolean = currentTokenCount.get() > 0L

    fun tryConsume(amount: Long = 1): Boolean {
        Checks.isWholeNumber(amount, "amount")

        try {
            logger.trace { "Preparing to enter lock in TokenBucket#tryConsume..." }
            lock.lock()
            if (currentTokenCount.get() >= amount) {
                currentTokenCount.addAndGet(-amount)
                return true
            }

            return false

        } finally {
            lock.unlock()
            logger.trace { "Successfully left lock in TokenBucket#tryConsume!" }
        }
    }

    override fun hashCode(): Int = HashCodeBuilder.builder(this::class)
        .append(this.tokenLimit)
        .append(this.refillTime).hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TokenBucket

        if (tokenLimit != other.tokenLimit) return false
        if (refillTime != other.refillTime) return false
        if (currentTokenCount != other.currentTokenCount) return false

        return true
    }
}