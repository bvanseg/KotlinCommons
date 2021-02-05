/*
 * MIT License
 *
 * Copyright (c) 2021 Boston Vanseghi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package bvanseg.kotlincommons.time.api

import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun Duration.toKhronoTime(): KhronoTime = KhronoTime(nano = this.nano.toDouble())
fun Duration.toMutableKhronoTime(): MutableKhronoTime = MutableKhronoTime(nano = this.nano.toDouble())

fun LocalDate.toKhronoDate(): KhronoDate = this.run {
    KhronoDate(
        d = this.dayOfMonth.toDouble(),
        mth = this.monthValue,
        yr = this.year.toDouble()
    )
}

fun LocalDate.toMutableKhronoDate(): MutableKhronoDate = this.run {
    MutableKhronoDate(
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

fun LocalTime.toMutableKhronoTime(): MutableKhronoTime = this.run {
    MutableKhronoTime(
        hr = this.hour.toDouble(),
        min = this.minute.toDouble(),
        sec = this.second.toDouble(),
        nano = this.nano.toDouble()
    )
}

fun LocalDateTime.toKhronoDate(): KhronoDate = this.toLocalDate().toKhronoDate()
fun LocalDateTime.toKhronoTime(): KhronoTime = this.toLocalTime().toKhronoTime()
fun LocalDateTime.toMutableKhronoDate(): MutableKhronoDate = this.toLocalDate().toMutableKhronoDate()
fun LocalDateTime.toMutableKhronoTime(): MutableKhronoTime = this.toLocalTime().toMutableKhronoTime()

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

fun LocalDateTime.toMutableKhronoDateTime(): MutableKhronoDateTime = this.run {
    MutableKhronoDateTime(
        day = this.dayOfMonth.toDouble(),
        month = this.monthValue,
        year = this.year.toDouble(),
        hour = this.hour.toDouble(),
        minute = this.minute.toDouble(),
        second = this.second.toDouble(),
        nanosecond = this.nano.toDouble()
    )
}