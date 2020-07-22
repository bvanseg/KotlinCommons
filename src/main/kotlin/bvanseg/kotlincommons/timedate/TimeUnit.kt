package bvanseg.kotlincommons.timedate

import java.time.temporal.ChronoUnit

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
        override val asMillis: Long
            get() = -1
        override val pronto: TimePerformer
            get() = TODO("This should not have been called!")
        override val exactly: TimePerformer
            get() = TODO("Not yet implemented")
    }

    data class Day(val days: Long): TimeContextUnit(days) {
        override val asHour: Long = days * 24
        override val asMinute: Long = asHour * 60
        override val asSeconds: Long = asMinute * 60
        override val asMillis: Long = asSeconds * 1000
        override val asNano: Long = asMillis * 1000000
        override val pronto: TimePerformer
            get() = TODO("Not yet implemented")
        override val exactly: TimePerformer
            get() = TODO("Not yet implemented")
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
        override val asMillis: Long
            get() = TODO()
        override val pronto: TimePerformer
            get() = TODO("Not yet implemented")
        override val exactly: TimePerformer
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
            get() = asMillis * 1000000
        override val asMillis: Long
            get() = asSeconds * 1000
        override val pronto: TimePerformer
            get() = TODO("Not yet implemented")
        override val exactly: TimePerformer
            get() = TODO("Not yet implemented")
    }

    data class Year(val years: Long): TimeContextUnit(years) {
        override val asHour: Long
            get() = TODO("Not yet implemented")
        override val asMinute: Long
            get() = TODO("Not yet implemented")
        override val asSeconds: Long
            get() = years * 12 * 4 * 7 * 24 * 60 * 60
        override val asNano: Long
            get() = asMillis * 1000000
        override val asMillis: Long
            get() = asSeconds * 1000
        override val pronto: TimePerformer
            get() = TODO("Not yet implemented")
        override val exactly: TimePerformer
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
            get() = asMillis * 1000000
        override val asMillis: Long
            get() = asSeconds * 1000
        override val pronto: TimePerformer
            get() = TODO("Not yet implemented")
        override val exactly: TimePerformer
            get() = TODO("Not yet implemented")
    }

    data class Minute(val minute: Long): TimeContextUnit(minute) {
        override val asHour: Long
            get() = minute / 60
        override val asMinute: Long
            get() = minute
        override val asSeconds: Long
            get() = minute * 60
        override val asNano: Long
            get() = asMillis * 1000000
        override val asMillis: Long
            get() = asSeconds * 1000
        override val pronto: TimePerformer
            get() = TODO("Not yet implemented")
        override val exactly: TimePerformer
            get() = TODO("Not yet implemented")
    }

    data class Second(val seconds: Long): TimeContextUnit(seconds) {
        override val asHour: Long
            get() = 60 / 60 / seconds
        override val asMinute: Long
            get() = 60 / seconds
        override val asSeconds: Long
            get() = seconds
        override val asNano: Long
            get() = asMillis * 1000000
        override val asMillis: Long
            get() = asSeconds * 1000
        override val pronto: TimePerformer
            get() = TODO("Not yet implemented")
        override val exactly: TimePerformer
            get() = TODO("Not yet implemented")
    }

    data class Nano(val nanosecs: Long): TimeContextUnit(nanosecs) {
        override val asHour: Long = 60 / 60 / 1000 / nanosecs
        override val asMinute: Long = 60 / 1000 / nanosecs
        override val asNano: Long = nanosecs
        override val asMillis: Long = nanosecs / 1000000
        override val asSeconds: Long = 1000000 / asMillis
        override val pronto: TimePerformer
            get() = TODO("Not yet implemented")
        override val exactly: TimePerformer
            get() = TODO("Not yet implemented")
    }
}

enum class TimeUnit{
    YEAR,
    MONTH,
    DAY,
    HOUR,
    MINUTE,
    SECOND,
    NANO
}

val years = TimeUnit.YEAR
val months = TimeUnit.MONTH
val days = TimeUnit.DAY
val hours = TimeUnit.HOUR
val minutes = TimeUnit.MINUTE
val seconds = TimeUnit.SECOND
val nanos = TimeUnit.NANO