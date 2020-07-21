package bvanseg.kotlincommons.timedate

import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

sealed class TimeContextUnit(val value: Long): TimeContext {

    fun asChrono() = when(this) {
        is Nano -> ChronoUnit.NANOS
        is Second -> ChronoUnit.SECONDS
        is Minute -> ChronoUnit.MINUTES
        is Hour -> ChronoUnit.HOURS
        is Day -> ChronoUnit.DAYS
        is Month -> ChronoUnit.MONTHS
        is Year -> ChronoUnit.YEARS
        else -> throw Exception("Failed to convert time context to chrono unit representation!")
    }

    object None: TimeContextUnit(-1) {
        override val asHour: Long
            get() = -1
        override val asMinute: Long
            get() = -1
        override val asSeconds: Long
            get() = -1
        override val asNano: Long
            get() = -1
    }

    data class Day(val days: Long): TimeContextUnit(days) {
        override val asHour: Long = days * 24
        override val asMinute: Long = asHour * 60
        override val asSeconds: Long = asMinute * 60
        override val asNano: Long = asSeconds * 1000
    }

    data class Week(val weeks: Long): TimeContextUnit(weeks) {
        override val asHour: Long
            get() = TODO("Not yet implemented")
        override val asMinute: Long
            get() = TODO("Not yet implemented")
        override val asSeconds: Long
            get() = weeks * 7 * 24 * 60 * 60
        override val asNano: Long
            get() = TODO("Not yet implemented")
    }

    data class Month(val months: Long): TimeContextUnit(months) {
        override val asHour: Long
            get() = months * 7 * 4 * 24
        override val asMinute: Long
            get() = asHour * 60
        override val asSeconds: Long
            get() = asMinute * 60
        override val asNano: Long
            get() = asSeconds * 1000
    }

    data class Year(val years: Long): TimeContextUnit(years) {
        override val asHour: Long
            get() = TODO("Not yet implemented")
        override val asMinute: Long
            get() = TODO("Not yet implemented")
        override val asSeconds: Long
            get() = years * 12 * 4 * 7 * 24 * 60 * 60
        override val asNano: Long
            get() = TODO("Not yet implemented")
    }

    data class Hour(val hours: Long): TimeContextUnit(hours) {
        override val asHour: Long
            get() = hours
        override val asMinute: Long
            get() = hours * 60
        override val asSeconds: Long
            get() = asMinute * 60
        override val asNano: Long
            get() = asSeconds * 1000
    }

    data class Minute(val minute: Long): TimeContextUnit(minute) {
        override val asHour: Long
            get() = minute / 60
        override val asMinute: Long
            get() = minute
        override val asSeconds: Long
            get() = minute * 60
        override val asNano: Long
            get() = asSeconds * 1000
    }

    data class Second(val seconds: Long): TimeContextUnit(seconds) {
        override val asHour: Long
            get() = 60 / 60 / seconds
        override val asMinute: Long
            get() = 60 / seconds
        override val asSeconds: Long
            get() = seconds
        override val asNano: Long
            get() = seconds * 1000
    }

    data class Nano(val nanosecs: Long): TimeContextUnit(nanosecs) {
        override val asHour: Long = 60 / 60 / 1000 / nanosecs
        override val asMinute: Long = 60 / 1000 / nanosecs
        override val asSeconds: Long = 1000 / nanosecs
        override val asNano: Long = nanosecs
    }

    /**
     * TODO: Convert to abstract property
     */
    fun getTimeMillis(): Long {
        val longValue = value
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