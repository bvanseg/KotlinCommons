package bvanseg.kotlincommons.timedate

import java.lang.Thread.sleep
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAccessor
import java.util.*

//data class TimeDate(val dateTime: LocalDateTime)
//
//class TimeDateBuilder{
//    var year = -1
//    var month = -1
//    var day = -1
//    var hour = -1
//    var minute = -1
//    var second = -1
//    var millis = -1
//    var nano = -1
//
//    internal fun buildLocal() = LocalDateTime.of(year, month, day, hour, minute, second)
//}
//
//fun localTime(callback: TimeDateBuilder.()->Unit): LocalDateTime{
//    val timeDateBuilder = TimeDateBuilder()
//    timeDateBuilder.callback()
//    return timeDateBuilder.buildLocal()
//}
//
////val Int.days get() = TimeUnit.Day(this)
////val Int.months get() = TimeUnit.Month(this)
////val Int.years get() = TimeUnit.Year(this)
////val Int.minutes get() = TimeUnit.Minute(this)
////val Int.hours get() = TimeUnit.Hour(this)
//////val Int.seconds get() = TimeUnit.Second(this)
////val Int.nanos get() = TimeUnit.Nano(this)
//
////val now get() = LocalDateTime.now()
//
//infix fun TimeUnit.Year.from(dateTime: LocalDateTime): LocalDateTime =
//        dateTime.plusYears(this.years.toLong())
//
//infix fun TimeUnit.Month.from(dateTime: LocalDateTime): LocalDateTime =
//        dateTime.plusMonths(this.months.toLong())
//
//infix fun TimeUnit.Day.from(dateTime: LocalDateTime): LocalDateTime =
//        dateTime.plusDays(this.days.toLong())
//
//infix fun TimeUnit.Minute.from(dateTime: LocalDateTime): LocalDateTime =
//        dateTime.plusDays(this.minute.toLong())
//
//infix fun TimeUnit.Hour.from(dateTime: LocalDateTime): LocalDateTime =
//        dateTime.plusHours(this.hours.toLong())
//
//infix fun TimeUnit.Second.from(dateTime: LocalDateTime): LocalDateTime =
//        dateTime.plusSeconds(this.seconds.toLong())
//
//infix fun TimeUnit.Nano.from(dateTime: LocalDateTime): LocalDateTime =
//        dateTime.plusNanos(this.nanosecs.toLong())
//
//infix fun TimeUnit.Year.before(dateTime: LocalDateTime): LocalDateTime =
//        dateTime.minusYears(this.years.toLong())
//
//infix fun TimeUnit.Month.before(dateTime: LocalDateTime): LocalDateTime =
//        dateTime.minusMonths(this.months.toLong())
//
//infix fun TimeUnit.Day.before(dateTime: LocalDateTime): LocalDateTime =
//        dateTime.minusDays(this.days.toLong())
//
//infix fun TimeUnit.Minute.before(dateTime: LocalDateTime): LocalDateTime =
//        dateTime.minusMinutes(this.minute.toLong())
//
//infix fun TimeUnit.Hour.before(dateTime: LocalDateTime): LocalDateTime =
//        dateTime.minusHours(this.hours.toLong())
//
//infix fun TimeUnit.Second.before(dateTime: LocalDateTime): LocalDateTime =
//        dateTime.minusSeconds(this.seconds.toLong())
//
//infix fun TimeUnit.Nano.before(dateTime: LocalDateTime): LocalDateTime =
//        dateTime.minusNanos(this.nanosecs.toLong())
//
//infix fun LocalDateTime.until(timeUnit: TimeUnit.Year): Long {
//    val end = this.truncatedTo(ChronoUnit.YEARS).plusYears(timeUnit.years.toLong())
//    return this.until(end, ChronoUnit.YEARS)
//}
//
//infix fun LocalDateTime.until(timeUnit: TimeUnit.Month): Long {
//    val end = this.truncatedTo(ChronoUnit.MONTHS).plusMonths(timeUnit.months.toLong())
//    return this.until(end, ChronoUnit.MONTHS)
//}
//
//infix fun LocalDateTime.until(timeUnit: TimeUnit.Day): Long {
//    val end = this.truncatedTo(ChronoUnit.DAYS).plusDays(timeUnit.days.toLong())
//    return this.until(end, ChronoUnit.DAYS)
//}
//
//infix fun LocalDateTime.until(timeUnit: TimeUnit.Hour): Long {
//    val end = this.truncatedTo(ChronoUnit.HOURS).plusHours(timeUnit.hours.toLong())
//    return this.until(end, ChronoUnit.HOURS)
//}
//
//infix fun LocalDateTime.until(timeUnit: TimeUnit.Minute): Long{
//    val end = this.truncatedTo(ChronoUnit.MINUTES).plusMinutes(timeUnit.minute.toLong())
//    return this.until(end, ChronoUnit.SECONDS)
//}
//
//infix fun LocalDateTime.until(timeUnit: TimeUnit.Second): Long{
//    val end = this.truncatedTo(ChronoUnit.SECONDS).plusSeconds(timeUnit.seconds.toLong())
//    return this.until(end, ChronoUnit.SECONDS)
//}
//
//infix fun LocalDateTime.until(timeUnit: TimeUnit.Nano): Long{
//    val end = this.truncatedTo(ChronoUnit.NANOS).plusSeconds(timeUnit.nanosecs.toLong())
//    return this.until(end, ChronoUnit.NANOS)
//}
//
//class NextTime{
//    val year get() = now until 1.years
//    val month get() = now until 1.months
//    val day get() = now until 1.days
//    val hour get() = now until 1.hours
//    val minute get() = now until 1.minutes
//    val second get() = now until 1.seconds
//    val nano get() = now until 1.nanos
//}
//
//val next get() = NextTime()
//
//sealed class TimeUnitEmpty{
//    object Years: TimeUnitEmpty()
//    object Months: TimeUnitEmpty()
//    object Days: TimeUnitEmpty()
//    object Hours: TimeUnitEmpty()
//    object Minutes: TimeUnitEmpty()
//    object Seconds: TimeUnitEmpty()
//    object Nano: TimeUnitEmpty()
//}
//
//val years get() = TimeUnitEmpty.Years
//val months get() = TimeUnitEmpty.Months
//val days get() = TimeUnitEmpty.Days
//val minutes get() = TimeUnitEmpty.Minutes
//val seconds get() = TimeUnitEmpty.Seconds
//val nanos get() = TimeUnitEmpty.Nano
//
//fun main(){
//    val tenYearsFromNow = 10.years from now
//    println(tenYearsFromNow)
////    val hundredMinsBeforeNow = 100.minutes before now in seconds
////    println(hundredMinsBeforeNow)
//    println(now)
//    println(now until 1.minutes)
//    println(now)
//    sleep(next.minute)
//    println(now)
//}