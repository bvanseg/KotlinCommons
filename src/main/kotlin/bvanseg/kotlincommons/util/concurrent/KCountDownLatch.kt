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
package bvanseg.kotlincommons.util.concurrent

import bvanseg.kotlincommons.lang.check.Checks
import bvanseg.kotlincommons.time.api.Khrono
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

/**
 * A coroutine-specific implementation of [java.util.concurrent.CountDownLatch].
 * Adapted from https://gist.github.com/konrad-kaminski/d7808070f4218349674589e1dc97264a.
 *
 * @author Boston Vanseghi
 * @author since 2.7.0
 */
class KCountDownLatch(initialCount: Int) {
    private var count = initialCount
    private val continuations = mutableListOf<Continuation<Unit>>()
    private val lock = ReentrantLock()

    init {
        Checks.isWholeNumber.check(initialCount, "initialCount")
    }

    fun countDown() {
        val doResume = lock.withLock {
            count != 0 && (--count == 0)
        }

        if (doResume) {
            continuations.forEach {
                it.resume(Unit)
            }

            continuations.clear()
        }
    }

    fun getCount() = lock.withLock { count.toLong() }

    suspend fun await(time: Khrono): Boolean = withTimeoutOrNull(time.toMillis().toLong()) { await() } != null

    suspend fun await() {
        var locked = true
        lock.lock()

        try {
            if (count > 0) {
                suspendCancellableCoroutine<Unit> { cont ->
                    continuations += cont
                    cont.invokeOnCancellation {
                        if (cont.isCancelled) {
                            lock.withLock {
                                continuations -= cont
                            }
                        }
                    }
                    lock.unlock()
                    locked = false
                }
            }
        } finally {
            if (locked) {
                lock.unlock()
            }
        }
    }
}