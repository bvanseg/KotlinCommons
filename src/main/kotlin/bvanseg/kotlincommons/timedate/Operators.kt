package bvanseg.kotlincommons.timedate

import bvanseg.kotlincommons.timedate.transformer.UntilContext
import java.lang.Exception
import java.util.concurrent.TimeUnit

/**
 * This should be done using a flatMap operator. Add an abstract method flatMap to [TimeContainer] such that
 * all TimeContainers can be flatMap'd. We also need a way of constructing timeObjects directly into a [TimeContainer],
 * perhaps as a primary constructor and have a secondary constructor for default time objects.
 *
 * ```
 * //Flatmap so we can transform timeObject property from old (passed into callback) to new (returned from callback)
 * val new = now.flatMap{
 *     Time(
 *          it.year,
 *          it.month,
 *          it.day,
 *          it.hour,
 *          it.minute,
 *          it.seconds + context.value,
 *          it.nano
 *     )
 * } //Should produce the same thing as [now] except it should keep the rest of it's numbers
 * ```
 */

operator fun LocalDateTimeContainer.plus(context: UnitBasedTimeContainer): UnitBasedTimeContainer {
    val here = this.toUnitBasedTimeContainer()
    return here + context
}

operator fun UnitBasedTimeContainer.plus(context: LocalDateTimeContainer): UnitBasedTimeContainer {
    return context + this
}

operator fun UnitBasedTimeContainer.plus(context: UnitBasedTimeContainer): UnitBasedTimeContainer {
    val unit = context.unit
    return flatmap {
        when(unit){
            is TimeContextUnit.Nano -> Time(it.year, it.month, it.day, it.hour, it.minute, it.second, it.nano + unit.nanosecs)
            is TimeContextUnit.Second -> Time(it.year, it.month, it.day, it.hour, it.minute, it.second + unit.seconds, it.nano)
            is TimeContextUnit.Minute -> Time(it.year, it.month, it.day, it.hour, it.minute + unit.minute, it.second, it.nano)
            is TimeContextUnit.Hour -> Time(it.year, it.month, it.day, it.hour + unit.hours, it.minute, it.second, it.nano)
            is TimeContextUnit.Day -> Time(it.year, it.month, it.day + unit.days, it.hour, it.minute, it.second, it.nano)
            is TimeContextUnit.Week -> Time(it.year, it.month, it.day + (unit.weeks * 7), it.hour, it.minute, it.second, it.nano)
            is TimeContextUnit.Month -> Time(it.year, it.month + unit.months, it.day, it.hour, it.minute, it.second, it.nano)
            is TimeContextUnit.Year -> Time(it.year + unit.years, it.month, it.day, it.hour, it.minute, it.second, it.nano)
            else -> TODO()
        }
    }
}

operator fun LocalDateTimeContainer.minus(context: UnitBasedTimeContainer): UnitBasedTimeContainer {
    val here = this.toUnitBasedTimeContainer()
    return here - context
}

operator fun UnitBasedTimeContainer.minus(context: LocalDateTimeContainer): UnitBasedTimeContainer {
    val here = context.toUnitBasedTimeContainer()
    return here - this
}

operator fun UnitBasedTimeContainer.minus(context: UnitBasedTimeContainer): UnitBasedTimeContainer {
    val ctx = context.unit
    return flatmap {
        when(ctx){
            is TimeContextUnit.Nano -> Time(it.year, it.month, it.day, it.hour, it.minute, it.second, it.nano - ctx.nanosecs)
            is TimeContextUnit.Second -> Time(it.year, it.month, it.day, it.hour, it.minute, it.second - ctx.seconds, it.nano)
            is TimeContextUnit.Minute -> Time(it.year, it.month, it.day, it.hour, it.minute - ctx.minute, it.second, it.nano)
            is TimeContextUnit.Hour -> Time(it.year, it.month, it.day, it.hour - ctx.hours, it.minute, it.second, it.nano)
            is TimeContextUnit.Day -> Time(it.year, it.month, it.day - ctx.days, it.hour, it.minute, it.second, it.nano)
            is TimeContextUnit.Week -> Time(it.year, it.month, it.day - (ctx.weeks * 7), it.hour, it.minute, it.second, it.nano)
            is TimeContextUnit.Month -> Time(it.year, it.month - ctx.months, it.day, it.hour, it.minute, it.second, it.nano)
            is TimeContextUnit.Year -> Time(it.year - ctx.years, it.month, it.day, it.hour, it.minute, it.second, it.nano)
            else -> TODO()
        }
    }
}

infix fun UnitBasedTimeContainer.after(context: LocalDateTimeContainer) = this + context
infix fun UnitBasedTimeContainer.from(context: LocalDateTimeContainer) = this + context
infix fun UnitBasedTimeContainer.before(context: LocalDateTimeContainer) = this - context

infix fun LocalDateTimeContainer.isBefore(context: LocalDateTimeContainer): Boolean = this.currentTime.isBefore(context.currentTime)
infix fun LocalDateTimeContainer.isAfter(context: LocalDateTimeContainer): Boolean = this.currentTime.isAfter(context.currentTime)

infix fun LocalDateTimeContainer.offset(context: UnitBasedTimeContainer): LocalDateTimeContainer {
    val ctx = context.unit
    return when(context.unit) {
        is TimeContextUnit.Nano -> LocalDateTimeContainer(this.currentTime.plusNanos(ctx.value))
        is TimeContextUnit.Second -> LocalDateTimeContainer(this.currentTime.plusSeconds(ctx.value))
        is TimeContextUnit.Minute -> LocalDateTimeContainer(this.currentTime.plusMinutes(ctx.value))
        is TimeContextUnit.Hour -> LocalDateTimeContainer(this.currentTime.plusHours(ctx.value))
        is TimeContextUnit.Day -> LocalDateTimeContainer(this.currentTime.plusDays(ctx.value))
        is TimeContextUnit.Month -> LocalDateTimeContainer(this.currentTime.plusMonths(ctx.value))
        is TimeContextUnit.Year -> LocalDateTimeContainer(this.currentTime.plusYears(ctx.value))
        else -> throw Exception("Unable to offset local date time container!") // TODO
    }
}

infix fun UnitBasedTimeContainer.into(context: TimeUnit): Long {
    return when(context) {
        TimeUnit.NANOSECONDS -> this.asNano
        TimeUnit.SECONDS -> this.asSeconds
        TimeUnit.MINUTES -> this.asMinute
        TimeUnit.HOURS -> this.asHour
        else -> TODO()
    }
}