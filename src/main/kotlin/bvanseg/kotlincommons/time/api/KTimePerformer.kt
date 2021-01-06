package bvanseg.kotlincommons.time.api

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime

/**
 *
 * @param frequency The [KTime] unit representing how often the performer should execute.
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
class KTimePerformer(val frequency: KTime, val action: (KTimePerformer) -> Unit, val counterDrift: Boolean = false) {

    var offset: Long = -1L
    var initDelay: Long = -1L
    var startDelay: Long = -1L

    @Volatile
    private var shouldStop: Boolean = false

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
    fun startAt(date: LocalDateTime): KTimePerformer {
        val odt = OffsetDateTime.now()
        val currentMillis = odt.toInstant().toEpochMilli()
        val millisToExecuteAt = date.toInstant(odt.offset).toEpochMilli()

        startDelay = millisToExecuteAt - currentMillis
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
    fun offset(time: KTime): KTimePerformer {
        offset = time.toMillis()
        return this
    }

    /**
     * Adds a delay to the performer.
     *
     * @param time The amount of time by which to delay the performer.
     */
    fun delay(time: KTime): KTimePerformer {

        initDelay = time.toMillis()
        return this
    }

    fun onException(cb: (Throwable) -> Unit): KTimePerformer {
        exceptionCallback = cb
        return this
    }

    fun stop() {
        shouldStop = true
    }

    /**
     * Adds a limit to how many times the performer executes. By default, performers execute forever.
     *
     * @param count The number of times the performer will execute before stopping.
     *
     * @throws IllegalStateException if the provided [count] is negative.
     */
    @Throws(IllegalStateException::class)
    fun limit(count: Long): KTimePerformer {

        if (count < 0) {
            throw IllegalStateException("Given count can not be negative: $count.")
        }

        limit = count
        return this
    }

    private val executor: suspend CoroutineScope.() -> Any = {

        // Date-based delay.
        if (startDelay > 0) {
            kotlinx.coroutines.delay(startDelay)
        }

        // Unit-based delay.
        if (initDelay > 0) {
            kotlinx.coroutines.delay(initDelay)
        }

        while (true) {
            if (shouldStop) {
                break
            }

            // Offset-based delay.
            if (offset > 0) {
                kotlinx.coroutines.delay(offset)
            }

            try {
                action(this@KTimePerformer)
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
                val delta = frequency.toMillis() - snapshotMillis % frequency.toMillis()
                kotlinx.coroutines.delay(delta)
            } else {
                kotlinx.coroutines.delay(frequency.toMillis())
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