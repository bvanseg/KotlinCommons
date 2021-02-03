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

val Number.nanoseconds
    get() = Khrono(this.toDouble(), KhronoUnit.NANOSECOND)

val Number.microseconds
    get() = Khrono(this.toDouble(), KhronoUnit.MICROSECOND)

val Number.milliseconds
    get() = Khrono(this.toDouble(), KhronoUnit.MILLISECOND)

val Number.seconds
    get() = Khrono(this.toDouble(), KhronoUnit.SECOND)

val Number.minutes
    get() = Khrono(this.toDouble(), KhronoUnit.MINUTE)

val Number.hours
    get() = Khrono(this.toDouble(), KhronoUnit.HOUR)

val Number.days
    get() = Khrono(this.toDouble(), KhronoUnit.DAY)

val Number.weeks
    get() = Khrono(this.toDouble(), KhronoUnit.WEEK)

val Number.years
    get() = Khrono(this.toDouble(), KhronoUnit.YEAR)

val Number.decades
    get() = Khrono(this.toDouble(), KhronoUnit.DECADE)

val Number.centuries
    get() = Khrono(this.toDouble(), KhronoUnit.CENTURY)

val Number.millenniums
    get() = Khrono(this.toDouble(), KhronoUnit.MILLENNIUM)

val nanoseconds: KhronoUnit = KhronoUnit.NANOSECOND
val microseconds: KhronoUnit = KhronoUnit.MICROSECOND
val milliseconds: KhronoUnit = KhronoUnit.MILLISECOND
val seconds: KhronoUnit = KhronoUnit.SECOND
val minutes: KhronoUnit = KhronoUnit.MINUTE
val hours: KhronoUnit = KhronoUnit.HOUR
val days: KhronoUnit = KhronoUnit.DAY
val halfDays: KhronoUnit = KhronoUnit.HALF_DAY
val weeks: KhronoUnit = KhronoUnit.WEEK
val years: KhronoUnit = KhronoUnit.YEAR
val decades: KhronoUnit = KhronoUnit.DECADE
val centuries: KhronoUnit = KhronoUnit.CENTURY
val millenniums: KhronoUnit = KhronoUnit.MILLENNIUM