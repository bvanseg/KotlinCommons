package bvanseg.kotlincommons.timedate

import bvanseg.kotlincommons.numbers.format
import bvanseg.kotlincommons.prettyprinter.buildPrettyString
import bvanseg.kotlincommons.timedate.transformer.into
import bvanseg.kotlincommons.timedate.transformer.until
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime

internal fun Time.checkAndCorrectNanoOverflow(): Time = if (nano >= 1_000_000_000) {
    //101000
    val overflow = nano / 1_000_000
    val nonOverflow = nano % 1_000_000
    Time(year, month, day, hour, minute, second, millis + overflow, nonOverflow)
} else {
    this
}

internal fun Time.checkAndCorrectMillisOverflow(): Time = if (millis >= 1_000) {
    //1010 == 1 second, 10ms
    val overflow = millis / 1_000
    val nonOverflow = millis % 1_000
    Time(year, month, day, hour, minute, second + overflow, nonOverflow, nano)
} else {
    this
}

internal fun Time.checkAndCorrectSecondOverflow(): Time = if(second >= 60){
    // 76
    val overflow = second / 60
    val nonOverflow = second % 60
    Time(year, month, day, hour, minute + overflow, nonOverflow, millis, nano)
} else {
    this
}


internal fun Time.checkAndCorrectMinuteOverflow(): Time = if(minute >= 60){
    // 76 = 1 hour, 16 minutes.
    val overflow = minute / 60
    val nonOverflow = minute % 60
    Time(year, month, day, hour + overflow, nonOverflow, second, millis, nano)
} else {
    this
}

internal fun Time.checkAndCorrectHourOverflow(): Time = if(hour >= 24){
    // 25 = 1 day, 1 hour.
    val overflow = hour / 24
    val nonOverflow = hour % 24
    Time(year, month, day + overflow, nonOverflow, minute, second, millis, nano)
} else {
    this
}

internal fun Time.checkAndCorrectOver(): Time =
    this.checkAndCorrectNanoOverflow()
        .checkAndCorrectMillisOverflow()
        .checkAndCorrectSecondOverflow()
        .checkAndCorrectMinuteOverflow()
        .checkAndCorrectHourOverflow()

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
            append("$year-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')} ${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}:${second.toString().padStart(2, '0')}.${millis.toString().padStart(3, '0')}/$nano")
        }
}

infix fun TimeContext.from(container: TimeContext) =
    container + this

val now get() = LocalDateTimeContainer(LocalDateTime.now())
val yesterday get() = 24.hours before now
val tomorrow get() = 24.hours from now