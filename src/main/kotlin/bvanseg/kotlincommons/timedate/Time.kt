package bvanseg.kotlincommons.timedate

import bvanseg.kotlincommons.prettyprinter.buildPrettyString
import bvanseg.kotlincommons.timedate.transformer.into
import bvanseg.kotlincommons.timedate.transformer.until
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.time.Instant
import java.time.LocalDateTime

data class Time(
    val year: Long,
    val month: Long,
    val day: Long,
    val hour: Long,
    val minute: Long,
    val second: Long,
    val millis: Long,
    val nano: Long
){
    @ExperimentalStdlibApi
    override fun toString(): String =
        buildPrettyString {
            append("$year/$month/$day/$hour:$minute:$second.$millis,$nano")
        }
}

infix fun TimeContext.from(container: TimeContext) =
    container + this

val now get() = LocalDateTimeContainer(LocalDateTime.now())
val yesterday get() = 24.hours before now
val tomorrow get() = 24.hours from now

fun main() = runBlocking {
    println(now + 1000.years)
    println(yesterday)
    println(tomorrow)
    println(now isBefore tomorrow)
    println(now isBefore (10.minutes from now))

    println(now until (1.minutes from now) into seconds)

    val start = now
//    sleep(1.minutes)
    println(start)
    /*
        now = 09:30:13.2000
        09:30:13
        09:31:13
        09:32:00
        09:33:00
     */
    (start until (10.minutes from start)
            every ((30.seconds.exactly)
            waitUntil (15.seconds.exactly)
            starting (25.seconds from now)))
        .perform {
            println("Hello, world! - ${Instant.now()}")
        }

//    val job = (start until (10.minutes from start)
//            every ((1.minutes.exactly)
//            waitUntil (1.minutes.exactly)
//            starting (1.minutes from now)))
//        .performAsync {
//            delay(1000)
//            println("Hello, world! - ${Instant.now()}")
//        }
//    job.join()

    println("Reached end of function.")
}