package bvanseg.kotlincommons.timedate

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
val yesterday get() = now offset (-24).hours
val tomorrow get() = now offset 24.hours

val nanoseconds = TimeUnit.NANOSECONDS
val seconds = TimeUnit.SECONDS
val minutes = TimeUnit.MINUTES
val hours = TimeUnit.HOURS
val days = TimeUnit.DAYS

//fun main() {
//    println(now + 1000.years)
//    println(yesterday)
//    println(tomorrow)
//    println(now isBefore tomorrow)
//
//    //println(now until 1.minutes into 1.seconds) // Should get 60 seconds as a result.
//
//    (now until now + 10.minutes everyExact 1.minutes).perform {
//        println("Hello, world! - ${Instant.now()}")
//    }
//
//    println("Reached end of function.")
//}