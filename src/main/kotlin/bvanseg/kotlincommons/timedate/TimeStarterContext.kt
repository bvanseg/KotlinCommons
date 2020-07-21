package bvanseg.kotlincommons.timedate

import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

interface TimeContext
interface BoundedContext: TimeContext

class UntilContext(val left: TimeContext, val right: TimeContext): BoundedContext

class TimeStarterContext(val currentTime: LocalDateTime, val contextUnit: TimeContextUnit = TimeContextUnit.None): TimeContext {

    override fun toString(): String = currentTime.toString()
}

class TimeTransformerContext: TimeContext

class TimeScheduleContext(val boundedContext: BoundedContext, val frequency: TimeStarterContext): TimeContext {

    fun perform(callback: () -> Unit) {
        var tracker = 0L

        while(true) {
            callback()
            Thread.sleep(frequency.contextUnit.getTimeMillis())
            tracker++
            if(tracker >= 10L) // TODO: Figure out times to iterate from bounded context.
                break
        }
    }

    fun performAsync(callback: () -> Unit) = Unit
}

sealed class TimeContextUnit(val value: Int) {
    object None: TimeContextUnit(-1)
    data class Day(val days: Int): TimeContextUnit(days)
    data class Week(val weeks: Int): TimeContextUnit(weeks)
    data class Month(val months: Int): TimeContextUnit(months)
    data class Year(val years: Int): TimeContextUnit(years)
    data class Hour(val hours: Int): TimeContextUnit(hours)
    data class Minute(val minute: Int): TimeContextUnit(minute)
    data class Second(val seconds: Int): TimeContextUnit(seconds)
    data class Nano(val nanosecs: Int): TimeContextUnit(nanosecs)

    fun getTimeMillis(): Long {
        val longValue = value.toLong()
        return when (this) {
            is Nano -> TimeUnit.MILLISECONDS.convert(longValue, TimeUnit.NANOSECONDS)
            is Second -> TimeUnit.MILLISECONDS.convert(longValue, TimeUnit.SECONDS)
            is Minute -> TimeUnit.MILLISECONDS.convert(longValue, TimeUnit.MINUTES)
            is Hour -> TimeUnit.MILLISECONDS.convert(longValue, TimeUnit.HOURS)
            is Day -> TimeUnit.MILLISECONDS.convert(longValue, TimeUnit.DAYS)
            else -> -1L
        }
    }
}

class TimeTerminalContext(val value: Long): TimeContext

val now: TimeStarterContext get() = TimeStarterContext(LocalDateTime.now())
val yesterday: TimeStarterContext get() = now - 24.hours
val tomorrow: TimeStarterContext get() = now + 24.hours

infix fun TimeContext.until(context: TimeContext) = UntilContext(this, context)
infix fun BoundedContext.every(context: TimeStarterContext) = TimeScheduleContext(this, context)
infix fun TimeContext.into(unit: TimeUnit): Nothing = TODO()

val Int.nanos get() = TimeStarterContext(now.currentTime.plusNanos(this.toLong()), TimeContextUnit.Nano(this))
val Int.seconds get() = TimeStarterContext(now.currentTime.plusSeconds(this.toLong()), TimeContextUnit.Second(this))
val Int.minutes get() = TimeStarterContext(now.currentTime.plusMinutes(this.toLong()), TimeContextUnit.Minute(this))
val Int.hours get() = TimeStarterContext(now.currentTime.plusHours(this.toLong()), TimeContextUnit.Hour(this))
val Int.days get() = TimeStarterContext(now.currentTime.plusDays(this.toLong()), TimeContextUnit.Day(this))
val Int.weeks get() = TimeStarterContext(now.currentTime.plusWeeks(this.toLong()), TimeContextUnit.Week(this))
val Int.months get() = TimeStarterContext(now.currentTime.plusMonths(this.toLong()), TimeContextUnit.Month(this))
val Int.years get() = TimeStarterContext(now.currentTime.plusYears(this.toLong()), TimeContextUnit.Year(this))

val nanoseconds = TimeUnit.NANOSECONDS
val seconds = TimeUnit.SECONDS
val minutes = TimeUnit.MINUTES
val hours = TimeUnit.HOURS
val days = TimeUnit.DAYS

operator fun TimeStarterContext.plus(context: TimeStarterContext) = when(context.contextUnit) {
    is TimeContextUnit.Nano -> TimeStarterContext(this.currentTime.plusNanos(context.contextUnit.nanosecs.toLong()))
    is TimeContextUnit.Second -> TimeStarterContext(this.currentTime.plusSeconds(context.contextUnit.seconds.toLong()))
    is TimeContextUnit.Minute -> TimeStarterContext(this.currentTime.plusMinutes(context.contextUnit.minute.toLong()))
    is TimeContextUnit.Hour -> TimeStarterContext(this.currentTime.plusHours(context.contextUnit.hours.toLong()))
    is TimeContextUnit.Day -> TimeStarterContext(this.currentTime.plusDays(context.contextUnit.days.toLong()))
    is TimeContextUnit.Month -> TimeStarterContext(this.currentTime.plusMonths(context.contextUnit.months.toLong()))
    is TimeContextUnit.Year -> TimeStarterContext(this.currentTime.plusYears(context.contextUnit.years.toLong()))
    else -> throw Exception("Failed to add times together!")
}

operator fun TimeStarterContext.minus(context: TimeStarterContext) = when(context.contextUnit) {
    is TimeContextUnit.Nano -> TimeStarterContext(this.currentTime.minusNanos(context.contextUnit.nanosecs.toLong()))
    is TimeContextUnit.Second -> TimeStarterContext(this.currentTime.minusSeconds(context.contextUnit.seconds.toLong()))
    is TimeContextUnit.Minute -> TimeStarterContext(this.currentTime.minusMinutes(context.contextUnit.minute.toLong()))
    is TimeContextUnit.Hour -> TimeStarterContext(this.currentTime.minusHours(context.contextUnit.hours.toLong()))
    is TimeContextUnit.Day -> TimeStarterContext(this.currentTime.minusDays(context.contextUnit.days.toLong()))
    is TimeContextUnit.Month -> TimeStarterContext(this.currentTime.minusMonths(context.contextUnit.months.toLong()))
    is TimeContextUnit.Year -> TimeStarterContext(this.currentTime.minusYears(context.contextUnit.years.toLong()))
    else -> throw Exception("Failed to add times together!")
}

fun main() {
    println(now + 1000.years)
    println(yesterday)
    println(tomorrow)

    //println(now until 1.minutes into 1.seconds) // Should get 60 seconds as a result.

    (now until tomorrow every 1.minutes).perform {
        println("Hello, world!")
    }

    println("test")
    now until tomorrow into seconds
}