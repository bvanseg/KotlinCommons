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
package bvanseg.kotlincommons.time

import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

private val fileSafeFormat = DateTimeFormatterBuilder()
    .parseCaseInsensitive()
    .append(DateTimeFormatter.ISO_LOCAL_DATE)
    .appendLiteral('_')
    .appendValue(ChronoField.HOUR_OF_DAY, 2)
    .appendLiteral('-')
    .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
    .appendLiteral('-')
    .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
    .optionalStart()
    .appendLiteral('-')
    .appendValue(ChronoField.MILLI_OF_SECOND, 3)
    .toFormatter()

/**
 * Converts this [LocalDateTime] to a [String] format which is safe to be used in file names
 */
fun LocalDateTime.toFileSafeString(): String = this.format(fileSafeFormat)

/**
 * Formats this duration to give a readable String in hours, minutes and seconds.
 * e.g. 1 hour, 30 minutes and 10 seconds
 *
 * @param millis Whether milliseconds should be included in the returned String
 */
fun Duration.format(millis: Boolean = false, shorthand: Boolean = false): String {
	val milliseconds = if (millis) this.toMillis() % 1000 else 0
	val seconds = this.seconds % 60
	val minutes = (this.seconds % 3600) / 60
	val hours = this.seconds / 3600

	val list = ArrayList<Pair<Long, String>>()
	if (hours > 0) list.add(hours to if (shorthand) "h" else "hour")
	if (minutes > 0) list.add(minutes to if (shorthand) "m" else "minute")
	if (seconds > 0) list.add(seconds to if (shorthand) "s" else "second")
	if (milliseconds > 0) list.add(milliseconds to if (shorthand) "ms" else "millisecond")

	val size = list.size
	if (size == 0) return if (shorthand) "0 ${if (millis) "m" else ""}s" else "0 ${if (millis) "milli" else ""}seconds"
	if (size == 1) return timePairToString(list[0], shorthand)

	val sb = StringBuilder()
	list.forEachIndexed { index, timePair ->
		sb.append(timePairToString(timePair, shorthand))
		when (size - index) {
			1 -> Unit
			2 -> sb.append(if (shorthand) ", " else " and ")
			else -> sb.append(", ")
		}
	}
	return sb.toString()
}

private fun timePairToString(pair: Pair<Long, String>, shorthand: Boolean): String =
	"${pair.first}${if (shorthand) "" else " "}${pair.second}${if (pair.first != 1L && !shorthand) "s" else ""}"
