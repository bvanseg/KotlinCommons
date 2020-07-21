package bvanseg.kotlincommons.timedate

import bvanseg.kotlincommons.prettyprinter.buildPrettyString
import java.time.LocalDateTime

interface TimeContainer: TimeContext{
    val timeObject: Time
//    val coerce: TimeContainer

    fun flatmap(cb: (Time) -> Time): TimeContainer
}

class LocalDateTimeContainer(val currentTime: LocalDateTime, override val timeObject: Time = Time(
    currentTime.year.toLong(),
    currentTime.month.value.toLong(),
    currentTime.dayOfWeek.value.toLong(),
    currentTime.hour.toLong(),
    currentTime.minute.toLong(),
    currentTime.second.toLong(),
    currentTime.nano.toLong()
)): TimeContainer {

    override fun flatmap(cb: (Time) -> Time): UnitBasedTimeContainer  {
        val newTimeObject = cb(timeObject)
        return UnitBasedTimeContainer(TimeContextUnit.None, newTimeObject)
    }

    override val asHour: Long
        get() = ((timeObject.year - 1970) * 365 * 24) +
                (timeObject.month * 4 * 7 * 24) +
                (timeObject.day * 7 * 24) +
                (timeObject.hour)

    override val asMinute: Long
        get() = ((timeObject.year - 1970) * 365 * 24 * 60) +
                (timeObject.month * 4 * 7 * 24 * 60) +
                (timeObject.day * 7 * 24 * 60) +
                (timeObject.hour * 60) +
                (timeObject.minute)

    override val asSeconds: Long
        get() = ((timeObject.year - 1970) * 365 * 24 * 60 * 60) +
                (timeObject.month * 4 * 7 * 24 * 60 * 60) +
                (timeObject.day * 7 * 24 * 60 * 60) +
                (timeObject.hour * 60 * 60) +
                (timeObject.minute * 60) +
                (timeObject.second)

    override val asNano: Long
        get() = ((timeObject.year - 1970) * 365 * 24 * 60 * 60 * 1000) +
                (timeObject.month * 4 * 7 * 24 * 60 * 60 * 1000) +
                (timeObject.day * 7 * 24 * 60 * 60 * 1000) +
                (timeObject.hour * 60 * 60 * 60 * 1000) +
                (timeObject.minute * 60 * 1000) +
                (timeObject.second * 1000)

    fun toUnitBasedTimeContainer() = UnitBasedTimeContainer(TimeContextUnit.Year(currentTime.year.toLong()), timeObject)

    override fun toString(): String = currentTime.toString()
}

class UnitBasedTimeContainer(val unit: TimeContextUnit, override val timeObject: Time =
    when(unit){
        is TimeContextUnit.Year -> Time(unit.years, 0, 0, 0, 0, 0, 0)
        is TimeContextUnit.Month -> Time(0, unit.months, 0, 0, 0, 0, 0)
        is TimeContextUnit.Week -> Time(0, 0, unit.weeks * 7, 0, 0, 0, 0)
        is TimeContextUnit.Day -> Time(0, 0, unit.days, 0, 0, 0, 0)
        is TimeContextUnit.Hour -> Time(0, 0, 0, unit.hours, 0, 0, 0)
        is TimeContextUnit.Minute -> Time(0, 0, 0, 0, unit.minute, 0, 0)
        is TimeContextUnit.Second -> Time(0, 0, 0, 0, 0, unit.seconds, 0)
        is TimeContextUnit.Nano -> Time(0, 0, 0, 0, 0, 0, unit.nanosecs)
        else -> Time(0, 0, 0, 0, 0, 0, 0)
    }): TimeContainer{

    override fun flatmap(cb: (Time) -> Time): UnitBasedTimeContainer  {
        val newTimeObject = cb(timeObject)
        return UnitBasedTimeContainer(unit, newTimeObject)
    }

    override val asHour: Long
        get() = ((timeObject.year - 1970) * 365 * 24) +
                (timeObject.month * 4 * 7 * 24) +
                (timeObject.day * 7 * 24) +
                (timeObject.hour)

    override val asMinute: Long
        get() = ((timeObject.year - 1970) * 365 * 24 * 60) +
                (timeObject.month * 4 * 7 * 24 * 60) +
                (timeObject.day * 7 * 24 * 60) +
                (timeObject.hour * 60) +
                (timeObject.minute)

    override val asSeconds: Long
        get() = ((timeObject.year - 1970) * 365 * 24 * 60 * 60) +
                (timeObject.month * 4 * 7 * 24 * 60 * 60) +
                (timeObject.day * 7 * 24 * 60 * 60) +
                (timeObject.hour * 60 * 60) +
                (timeObject.minute * 60) +
                (timeObject.second)

    override val asNano: Long
        get() = ((timeObject.year - 1970) * 365 * 24 * 60 * 60 * 1000) +
                (timeObject.month * 4 * 7 * 24 * 60 * 60 * 1000) +
                (timeObject.day * 7 * 24 * 60 * 60 * 1000) +
                (timeObject.hour * 60 * 60 * 60 * 1000) +
                (timeObject.minute * 60 * 1000) +
                (timeObject.second * 1000)

    @ExperimentalStdlibApi
    override fun toString(): String =
        buildPrettyString {
            append("${timeObject.year}/${timeObject.month}/${timeObject.day}/${timeObject.hour}:${timeObject.minute}:${timeObject.second}.${timeObject.nano}")
        }
}

val Int.nanos get() = UnitBasedTimeContainer(TimeContextUnit.Nano(this.toLong()))
val Int.seconds get() = UnitBasedTimeContainer(TimeContextUnit.Second(this.toLong()))
val Int.minutes get() = UnitBasedTimeContainer(TimeContextUnit.Minute(this.toLong()))
val Int.hours get() = UnitBasedTimeContainer(TimeContextUnit.Hour(this.toLong()))
val Int.days get() = UnitBasedTimeContainer(TimeContextUnit.Day(this.toLong()))
val Int.weeks get() = UnitBasedTimeContainer(TimeContextUnit.Week(this.toLong()))
val Int.months get() = UnitBasedTimeContainer(TimeContextUnit.Month(this.toLong()))
val Int.years get() = UnitBasedTimeContainer(TimeContextUnit.Year(this.toLong()))