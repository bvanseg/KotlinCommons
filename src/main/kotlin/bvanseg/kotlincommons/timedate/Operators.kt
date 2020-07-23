package bvanseg.kotlincommons.timedate

import bvanseg.kotlincommons.timedate.transformer.BoundedContext
import java.lang.Exception
import java.util.concurrent.TimeUnit

internal fun TimeContainer.checkAndCorrectNanoOverflow(): TimeContainer =
    this.flatmap { it: Time ->
        it.checkAndCorrectNanoOverflow()
    }

internal fun TimeContainer.checkAndCorrectMillisOverflow(): TimeContainer =
    this.flatmap { it: Time ->
        it.checkAndCorrectMillisOverflow()
    }

internal fun TimeContainer.checkAndCorrectSecondOverflow(): TimeContainer =
    this.flatmap { it: Time ->
        it.checkAndCorrectSecondOverflow()
    }


internal fun TimeContainer.checkAndCorrectMinuteOverflow(): TimeContainer =
    this.flatmap { it: Time ->
        it.checkAndCorrectMinuteOverflow()
    }

internal fun TimeContainer.checkAndCorrectHourOverflow(): TimeContainer =
    this.flatmap { it: Time ->
        it.checkAndCorrectHourOverflow()
    }

internal fun <T: TimeContainer> T.checkAndCorrectOver(): T =
    this.checkAndCorrectNanoOverflow()
        .checkAndCorrectMillisOverflow()
        .checkAndCorrectSecondOverflow()
        .checkAndCorrectMinuteOverflow()
        .checkAndCorrectHourOverflow() as T

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
    // Prefer the unit of the unit-based time container (context.unit) over the local date's 'None' unit.
    val here = this.toUnitBasedTimeContainer(context.unit)
    return here + context
}

operator fun UnitBasedTimeContainer.plus(context: LocalDateTimeContainer): UnitBasedTimeContainer {
    return context + this
}

operator fun LocalDateTimeContainer.plus(context: TimeContext): UnitBasedTimeContainer =
    when(context){
        is BoundedContext -> this + (context.right - context.left)
        is TimePerformer -> this + context.inner
        is TimeContainer ->
            when(context){
                is LocalDateTimeContainer -> (this + context).checkAndCorrectOver() as UnitBasedTimeContainer
                is UnitBasedTimeContainer -> (this + context).checkAndCorrectOver() as UnitBasedTimeContainer
                else -> TODO("Not sure what you did but I like it ;)")
            }
        else -> TODO("Not sure what you did but I like it ;)")
    }

operator fun TimeContext.plus(context: TimeContext): UnitBasedTimeContainer =
    when(this){
        is BoundedContext -> this + context
        is TimePerformer -> this.inner + context // FIXME: Can potentially cause a stackoverflow
        is TimeContainer -> when(this){
            is LocalDateTimeContainer -> this + context
            is UnitBasedTimeContainer -> this + context
            else -> TODO("Not sure what you did but I like it ;)")
        }
        else -> TODO("Not sure what you did but I like it ;)")
    }

operator fun BoundedContext.plus(context: TimeContext): UnitBasedTimeContainer {
    return (context + (right + left))
}

operator fun UnitBasedTimeContainer.plus(context: UnitBasedTimeContainer): UnitBasedTimeContainer {
    val unit = context.unit
    return (flatmap { it: Time ->
        when(unit){
            is TimeContextUnit.Nano -> Time(it.year, it.month, it.day, it.hour, it.minute, it.second, it.millis, it.nano + unit.nanosecs)
            is TimeContextUnit.Millis -> Time(it.year, it.month, it.day, it.hour, it.minute, it.second, it.millis + unit.millisecs, it.nano)
            is TimeContextUnit.Second -> Time(it.year, it.month, it.day, it.hour, it.minute, it.second + unit.seconds, it.millis, it.nano)
            is TimeContextUnit.Minute -> Time(it.year, it.month, it.day, it.hour, it.minute + unit.minute, it.second, it.millis, it.nano)
            is TimeContextUnit.Hour -> Time(it.year, it.month, it.day, it.hour + unit.hours, it.minute, it.second, it.millis, it.nano)
            is TimeContextUnit.Day -> Time(it.year, it.month, it.day + unit.days, it.hour, it.minute, it.second, it.millis, it.nano)
            is TimeContextUnit.Week -> Time(it.year, it.month, it.day + (unit.weeks * 7), it.hour, it.minute, it.second, it.millis, it.nano)
            is TimeContextUnit.Month -> Time(it.year, it.month + unit.months, it.day, it.hour, it.minute, it.second, it.millis, it.nano)
            is TimeContextUnit.Year -> Time(it.year + unit.years, it.month, it.day, it.hour, it.minute, it.second, it.millis, it.nano)
            else -> TODO()
        }
    } as UnitBasedTimeContainer).checkAndCorrectOver()
}

operator fun TimeContextUnit.plus(context: LocalDateTimeContainer): UnitBasedTimeContainer {
    val here = UnitBasedTimeContainer(this)
    return here + context
}

operator fun TimeContextUnit.plus(context: UnitBasedTimeContainer): UnitBasedTimeContainer {
    val here = UnitBasedTimeContainer(this)
    return here + context
}

operator fun TimeContextUnit.plus(context: TimeContextUnit): UnitBasedTimeContainer {
    val here = UnitBasedTimeContainer(this)
    val there = UnitBasedTimeContainer(context)
    return here + there
}

operator fun LocalDateTimeContainer.minus(context: UnitBasedTimeContainer): UnitBasedTimeContainer {
    val here = this.toUnitBasedTimeContainer()
    return here - context
}

operator fun LocalDateTimeContainer.minus(context: TimeContext): UnitBasedTimeContainer =
    when(context){
        is BoundedContext -> this - (context.right - context.left)
        is TimePerformer -> this - context.inner
        is TimeContainer ->
            when(context){
                is LocalDateTimeContainer -> this - context
                is UnitBasedTimeContainer -> this - context
                else -> TODO("Not sure what you did but I like it ;)")
            }
        else -> TODO("Not sure what you did but I like it ;)")
    }

operator fun UnitBasedTimeContainer.minus(context: TimeContext): UnitBasedTimeContainer =
    when(context){
        is BoundedContext -> this - (context.right - context.left)
        is TimePerformer -> this - context.inner
        is TimeContainer ->
            when(context){
                is LocalDateTimeContainer -> this - context
                is UnitBasedTimeContainer -> this - context
                else -> TODO("Not sure what you did but I like it ;)")
            }
        else -> TODO("Not sure what you did but I like it ;)")
    }

operator fun UnitBasedTimeContainer.minus(context: LocalDateTimeContainer): UnitBasedTimeContainer {
    val here = context.toUnitBasedTimeContainer()
    return here - this
}

operator fun TimeContextUnit.minus(context: LocalDateTimeContainer): UnitBasedTimeContainer {
    val here = UnitBasedTimeContainer(this)
    return here - context
}

operator fun TimeContextUnit.minus(context: UnitBasedTimeContainer): UnitBasedTimeContainer {
    val here = UnitBasedTimeContainer(this)
    return here - context
}

operator fun TimeContextUnit.minus(context: TimeContextUnit): UnitBasedTimeContainer {
    return UnitBasedTimeContainer(when(this){
        is TimeContextUnit.Year ->
            TimeContextUnit.Year(this.value - context.value)
        is TimeContextUnit.Month ->
            TimeContextUnit.Month(this.value - context.value)
        is TimeContextUnit.Day ->
            TimeContextUnit.Day(this.value - context.value)
        is TimeContextUnit.Hour ->
            TimeContextUnit.Hour(this.value - context.value)
        is TimeContextUnit.Minute ->
            TimeContextUnit.Minute(this.value - context.value)
        is TimeContextUnit.Second ->
            TimeContextUnit.Second(this.value - context.value)
        is TimeContextUnit.Millis ->
            TimeContextUnit.Millis(this.value - context.value)
        is TimeContextUnit.Nano ->
            TimeContextUnit.Nano(this.value - context.value)
        is TimeContextUnit.None -> TimeContextUnit.None
        else -> TODO()
    })
}

operator fun TimeContext.minus(context: TimeContext): UnitBasedTimeContainer =
    when(this){
        is BoundedContext -> this - context
        is TimePerformer -> this.inner - context
        is TimeContainer -> when(this){
            is LocalDateTimeContainer -> this - context
            is UnitBasedTimeContainer -> this - context
            else -> TODO("Not sure what you did but I like it ;)")
        }
        is TimeContextUnit -> this - context
        else -> TODO("Not sure what you did but I like it ;)")
    }

operator fun BoundedContext.minus(context: TimeContext): UnitBasedTimeContainer {
    return (context - (right - left))
}

operator fun UnitBasedTimeContainer.minus(context: UnitBasedTimeContainer): UnitBasedTimeContainer {
    val ctx = context.unit
    return flatmap { it: Time ->
        when(ctx){
            is TimeContextUnit.Nano -> Time(it.year, it.month, it.day, it.hour, it.minute, it.second, it.millis, it.nano - ctx.nanosecs)
            is TimeContextUnit.Millis -> Time(it.year, it.month, it.day, it.hour, it.minute, it.second, it.millis - ctx.millisecs, it.nano)
            is TimeContextUnit.Second -> Time(it.year, it.month, it.day, it.hour, it.minute, it.second - ctx.seconds, it.millis, it.nano)
            is TimeContextUnit.Minute -> Time(it.year, it.month, it.day, it.hour, it.minute - ctx.minute, it.second, it.millis, it.nano)
            is TimeContextUnit.Hour -> Time(it.year, it.month, it.day, it.hour - ctx.hours, it.minute, it.second, it.millis, it.nano)
            is TimeContextUnit.Day -> Time(it.year, it.month, it.day - ctx.days, it.hour, it.minute, it.second, it.millis, it.nano)
            is TimeContextUnit.Week -> Time(it.year, it.month, it.day - (ctx.weeks * 7), it.hour, it.minute, it.second, it.millis, it.nano)
            is TimeContextUnit.Month -> Time(it.year, it.month - ctx.months, it.day, it.hour, it.minute, it.second, it.millis, it.nano)
            is TimeContextUnit.Year -> Time(it.year - ctx.years, it.month, it.day, it.hour, it.minute, it.second, it.millis, it.nano)
            else -> TODO()
        }
    } as UnitBasedTimeContainer
}

infix fun UnitBasedTimeContainer.isBefore(context: TimeContextUnit) = this isBefore UnitBasedTimeContainer(context)
infix fun TimeContextUnit.isBefore(context: TimeContextUnit) = UnitBasedTimeContainer(this) isBefore UnitBasedTimeContainer(context)
infix fun UnitBasedTimeContainer.isAfter(context: TimeContextUnit) = this isAfter UnitBasedTimeContainer(context)
infix fun TimeContextUnit.isAfter(context: TimeContextUnit) = UnitBasedTimeContainer(this) isAfter UnitBasedTimeContainer(context)

infix fun UnitBasedTimeContainer.after(context: LocalDateTimeContainer) = this + context
infix fun UnitBasedTimeContainer.from(context: LocalDateTimeContainer) = this + context
infix fun UnitBasedTimeContainer.before(context: LocalDateTimeContainer) = this - context
infix fun LocalDateTimeContainer.before(context: LocalDateTimeContainer) = this - context
infix fun TimeContext.before(context: LocalDateTimeContainer) = this - context
infix fun TimeContext.before(context: TimeContext) = this - context

infix fun UnitBasedTimeContainer.isBefore(context: UnitBasedTimeContainer): Boolean = this.asMillis < context.asMillis

infix fun LocalDateTimeContainer.isBefore(context: UnitBasedTimeContainer): Boolean = this.toUnitBasedTimeContainer() isBefore context
infix fun LocalDateTimeContainer.isBefore(context: LocalDateTimeContainer): Boolean = this.toUnitBasedTimeContainer() isBefore context.toUnitBasedTimeContainer()

infix fun UnitBasedTimeContainer.isAfter(context: UnitBasedTimeContainer): Boolean = this.asMillis > context.asMillis

infix fun LocalDateTimeContainer.isAfter(context: UnitBasedTimeContainer): Boolean = this.toUnitBasedTimeContainer() isAfter context
infix fun LocalDateTimeContainer.isAfter(context: LocalDateTimeContainer): Boolean = this.toUnitBasedTimeContainer() isAfter context.toUnitBasedTimeContainer()

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
        else -> throw Exception("Unable to offset local date time container!")
    }
}

infix fun UnitBasedTimeContainer.into(context: TimeUnit): UnitBasedTimeContainer {
    return when(context) {
        TimeUnit.NANOSECONDS -> {
            val nano = this.asNano
            val result = flatmap { _: Time ->
                Time(0, 0, 0, 0, 0, 0, 0, nano)
            }
            result.flatmap { _: TimeContextUnit ->
                TimeContextUnit.Nano(nano)
            } as UnitBasedTimeContainer
        }
        TimeUnit.MILLISECONDS -> {
            val millis = this.asMillis
            val result = flatmap { _: Time ->
                Time(0, 0, 0, 0, 0, 0, millis, 0)
            }
            result.flatmap { _: TimeContextUnit ->
                TimeContextUnit.Nano(millis)
            } as UnitBasedTimeContainer
        }
        TimeUnit.SECONDS -> {
            val second = this.asSeconds
            val result = flatmap { _: Time ->
                Time(0, 0, 0, 0, 0, second, 0, 0)
            }
            result.flatmap { _: TimeContextUnit ->
                TimeContextUnit.Second(second)
            } as UnitBasedTimeContainer
        }
        TimeUnit.MINUTES -> {
            val minute = this.asMinute
            val result = flatmap { _: Time ->
                Time(0, 0, 0, 0, minute, 0, 0, 0)
            }
            result.flatmap { _: TimeContextUnit ->
                TimeContextUnit.Minute(minute)
            } as UnitBasedTimeContainer
        }
        TimeUnit.HOURS -> {
            val hour = this.asHour
            val result = flatmap { _: Time ->
                Time(0, 0, 0, hour, 0, 0, 0, 0)
            }
            result.flatmap { _: TimeContextUnit ->
                TimeContextUnit.Hour(hour)
            } as UnitBasedTimeContainer
        }
        else -> TODO()
    }
}