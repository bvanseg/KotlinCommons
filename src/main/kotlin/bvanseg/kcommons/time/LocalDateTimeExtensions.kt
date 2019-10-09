package bvanseg.kcommons.time

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