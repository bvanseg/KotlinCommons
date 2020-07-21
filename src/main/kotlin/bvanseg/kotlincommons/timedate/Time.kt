package bvanseg.kotlincommons.timedate

import bvanseg.kotlincommons.timedate.transformer.BoundedContext
import bvanseg.kotlincommons.timedate.transformer.UntilContext
import bvanseg.kotlincommons.timedate.transformer.until
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

data class Time(
    val year: Long,
    val month: Long,
    val day: Long,
    val hour: Long,
    val minute: Long,
    val second: Long,
    val nano: Long
)

val now get() = LocalDateTimeContainer(LocalDateTime.now())
val yesterday get() = now - 24.hours
val tomorrow get() = now + 24.hours

infix fun TimeContext.into(unit: TimeUnit): Nothing = TODO()

val nanoseconds = TimeUnit.NANOSECONDS
val seconds = TimeUnit.SECONDS
val minutes = TimeUnit.MINUTES
val hours = TimeUnit.HOURS
val days = TimeUnit.DAYS

fun main() {
    println(now + 1000.years)
    println(yesterday)
    println(tomorrow)

    //println(now until 1.minutes into 1.seconds) // Should get 60 seconds as a result.

    (now until now + 10.minutes every 1.minutes).perform {
        println("Hello, world!")
    }

    println("test")
//    now until tomorrow into seconds
}