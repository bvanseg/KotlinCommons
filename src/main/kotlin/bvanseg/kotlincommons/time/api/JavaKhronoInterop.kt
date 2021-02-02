package bvanseg.kotlincommons.time.api

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun LocalDate.toKhronoDate(): KhronoDate = this.run {
    KhronoDate(
        d = this.dayOfMonth.toDouble(),
        mth = this.monthValue,
        yr = this.year.toDouble()
    )
}

fun LocalTime.toKhronoTime(): KhronoTime = this.run {
    KhronoTime(
        hr = this.hour.toDouble(),
        min = this.minute.toDouble(),
        sec = this.second.toDouble(),
        nano = this.nano.toDouble()
    )
}

fun LocalDateTime.toKhronoDate(): KhronoDate = this.toLocalDate().toKhronoDate()
fun LocalDateTime.toKhronoTime(): KhronoTime = this.toLocalTime().toKhronoTime()

fun LocalDateTime.toKhronoDateTime(): KhronoDateTime = this.run {
    KhronoDateTime(
        day = this.dayOfMonth.toDouble(),
        month = this.monthValue,
        year = this.year.toDouble(),
        hour = this.hour.toDouble(),
        minute = this.minute.toDouble(),
        second = this.second.toDouble(),
        nanosecond = this.nano.toDouble()
    )
}