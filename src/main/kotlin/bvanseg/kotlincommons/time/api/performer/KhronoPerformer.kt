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
package bvanseg.kotlincommons.time.api.performer

import bvanseg.kotlincommons.lang.checks.Checks
import bvanseg.kotlincommons.time.api.Khrono
import bvanseg.kotlincommons.time.api.KhronoTime
import bvanseg.kotlincommons.time.api.milliseconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime

/**
 *
 * @param frequency The [Khrono] unit representing how often the performer should execute.
 * @param action The action to perform every [frequency].
 * @param counterDrift Whether or not the performer should avoid drifting. Because every [action] has some performance
 * overhead to execute, the time at which the action executes is offset by the previous execution's timelapse. To avoid
 * this, the performer is capable of countering the drift by offsetting the sleep/delay by the previous execution's
 * timelapse.
 *
 * For example, say you want to execute a task every minute. But the task takes 5 seconds to execute. You start at 00:00:00 with
 * a 1 minute delay, begin execution at 00:01:00, and finish execution at 00:01:05. Without counter drift, the next execution
 * time would be 00:02:05. With counter drifting, the next execution time would be 00:02:00, the next minute.
 *
 * @author Boston Vanseghi
 * @since 2.7.0
 */
class KhronoPerformer(val frequency: Khrono, val action: (KhronoPerformer) -> Unit, val counterDrift: Boolean = false) {

    var offset: Long = -1L
    var timeDelay: Long = -1L
    var startDateDelay: Long = -1L

    @Volatile
    var shouldStop: Boolean = false
        private set

    private var exceptionCallback: (Throwable) -> Unit = {
        it.printStackTrace()
    }

    private var timesExecuted: Long = 0L
    private var limit: Long = 0L

    /**
     * Sets the time that the performer will wait until in order to start.
     *
     * @param date The date for the time performer to start at.
     */
    fun startAt(date: LocalDateTime): KhronoPerformer {
        val odt = OffsetDateTime.now()
        val currentMillis = odt.toInstant().toEpochMilli()
        val millisToExecuteAt = date.toInstant(odt.offset).toEpochMilli()

        startDateDelay = millisToExecuteAt - currentMillis
        return this
    }

    fun stop() {
        shouldStop = true
    }

    /**
     * Adds an offset to occur before every action execution.
     *
     * This is particularly useful if you wish to execute a task every 3rd minute, for example. To execute a task every
     * 3rd minute, one simply needs to set the frequency to every hour and then the offset to 3 minutes.
     *
     * @param time The amount of time by which to offset the current action.
     */
    fun offset(time: Khrono): KhronoPerformer {
        offset = time.toMillis().toLong()
        return this
    }

    /**
     * Adds an offset to occur before every action execution.
     *
     * This is particularly useful if you wish to execute a task every 3rd minute, for example. To execute a task every
     * 3rd minute, one simply needs to set the frequency to every hour and then the offset to 3 minutes.
     *
     * @param time The amount of time by which to offset the current action.
     */
    fun offset(time: KhronoTime): KhronoPerformer = offsetMillis(time.asMillis.toLong())

    fun offsetMillis(millis: Long) = offset(millis.milliseconds)

    /**
     * Adds a delay to the performer.
     *
     * @param time The amount of time by which to delay the performer.
     */
    fun delay(time: Khrono): KhronoPerformer {

        timeDelay = time.toMillis().toLong()
        return this
    }

    /**
     * Adds a delay to the performer.
     *
     * @param time The amount of time by which to delay the performer.
     */
    fun delay(time: KhronoTime): KhronoPerformer = delayMillis(time.asMillis.toLong())

    fun delayMillis(millis: Long): KhronoPerformer = delay(millis.milliseconds)

    fun onException(cb: (Throwable) -> Unit): KhronoPerformer {
        exceptionCallback = cb
        return this
    }

    /**
     * Adds a limit to how many times the performer executes. By default, performers execute forever.
     *
     * @param count The number of times the performer will execute before stopping.
     */
    fun limit(count: Long): KhronoPerformer {
        Checks.isWholeNumber(count, "count")
        limit = count
        return this
    }

    private val executor: suspend CoroutineScope.() -> Any = {

        // Date-based delay.
        if (startDateDelay > 0) {
            delay(startDateDelay)
        }

        // Unit-based delay.
        if (timeDelay > 0) {
            delay(timeDelay)
        }

        while (true) {
            if (shouldStop) {
                break
            }

            // Offset-based delay.
            if (offset > 0) {
                delay(offset)
            }

            try {
                action(this@KhronoPerformer)
                timesExecuted++
            } catch (e: Exception) {
                exceptionCallback(e)
            }

            if (limit > 0 && timesExecuted >= limit) {
                break
            }

            if (counterDrift) {
                // Get current time accounting for offsets.
                val snapshotMillis = Instant.now().toEpochMilli() + OffsetDateTime.now().offset.totalSeconds * 1000L
                // Get the delta between the next interval and the current time.
                val delta = frequency.toMillis().toLong() - snapshotMillis % frequency.toMillis().toLong()
                delay(delta)
            } else {
                delay(frequency.toMillis().toLong())
            }
        }
    }

    /**
     * Runs the performer's internal executor.
     *
     * @param async Sets whether or not the performer will execute its task asynchronously using Kotlin's [CoroutineScope].
     * By default, performers execute blocking.
     */
    fun execute(async: Boolean = false) {
        if (async) {
            GlobalScope.launch {
                executor(this)
            }
        } else {
            runBlocking {
                executor(this)
            }
        }
    }

    operator fun invoke(async: Boolean = false) = execute(async)
}