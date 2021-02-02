package bvanseg.kotlincommons.util.concurrent

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
        if (initialCount < 0) {
            throw IllegalArgumentException("Initial count must be greater than or equal to 0!")
        }
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