package bvanseg.kotlincommons.timedate

import java.time.LocalDateTime

interface TimeContainer: TimeContext{
    val timeObject: Time
    val unit: TimeContextUnit

    @Suppress("INAPPLICABLE_JVM_NAME")
    @JvmName("flatmapTime")
    fun flatmap(cb: (time: Time) -> Time): TimeContainer  {
        val newTimeObject = cb(timeObject)
        return UnitBasedTimeContainer(unit, newTimeObject)
    }

    @Suppress("INAPPLICABLE_JVM_NAME")
    @JvmName("flatmapUnit")
    fun flatmap(cb: (unit: TimeContextUnit) -> TimeContextUnit): TimeContainer{
        val newTimeUnit = cb(unit)
        return UnitBasedTimeContainer(newTimeUnit, timeObject)
    }
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
    override val unit: TimeContextUnit = TimeContextUnit.Year(timeObject.year)

    override val asHour: Long
        get() =
            if(timeObject.year == 0L) {
                (timeObject.month * 4 * 7 * 24) +
                        (timeObject.day * 7 * 24) +
                        (timeObject.hour)
            }else{
                ((timeObject.year - 1970) * 365 * 24) +
                        (timeObject.month * 4 * 7 * 24) +
                        (timeObject.day * 7 * 24) +
                        (timeObject.hour)
            }

    override val asMinute: Long
        get() = (asHour * 60) +
                (timeObject.minute)

    override val asSeconds: Long
        get() = (asMinute * 60) + timeObject.second

    override val asNano: Long
        get() = (asSeconds * 1000 * 1000000) + timeObject.nano

    override val asMillis: Long = asSeconds * 1000

    override val pronto: TimePerformer
        get(){
            val performer = TimePerformer(this)
            return performer.pronto
        }
    override val exactly: TimePerformer
        get(){
            val performer = TimePerformer(this)
            return performer.exactly
        }

    fun toUnitBasedTimeContainer() = UnitBasedTimeContainer(TimeContextUnit.Year(currentTime.year.toLong()), timeObject)

    @ExperimentalStdlibApi
    override fun toString(): String = timeObject.toString()
}

class UnitBasedTimeContainer(override val unit: TimeContextUnit, override val timeObject: Time =
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

    override val asHour: Long
        get() =
            if(timeObject.year == 0L) {
                (timeObject.month * 4 * 7 * 24) +
                    (timeObject.day * 7 * 24) +
                    (timeObject.hour)
            }else{
                ((timeObject.year - 1970) * 365 * 24) +
                    (timeObject.month * 4 * 7 * 24) +
                    (timeObject.day * 7 * 24) +
                    (timeObject.hour)
            }
    override val asMinute: Long
        get() = (asHour * 60) +
            (timeObject.minute)

    override val asSeconds: Long
        get() = (asMinute * 60) + timeObject.second

    override val asMillis: Long
        get() = asSeconds * 1000

    override val asNano: Long
        get() = (asMillis * 1000000) + timeObject.nano

    override val pronto: TimePerformer
        get() = TimePerformer(this)

    override val exactly: TimePerformer
        get(){
            val performer = TimePerformer(this)
            return performer.exactly
        }

    @ExperimentalStdlibApi
    override fun toString(): String = timeObject.toString()

}

val Int.nanos get() = UnitBasedTimeContainer(TimeContextUnit.Nano(this.toLong()))
val Int.seconds get() = UnitBasedTimeContainer(TimeContextUnit.Second(this.toLong()))
val Int.minutes get() = UnitBasedTimeContainer(TimeContextUnit.Minute(this.toLong()))
val Int.hours get() = UnitBasedTimeContainer(TimeContextUnit.Hour(this.toLong()))
val Int.days get() = UnitBasedTimeContainer(TimeContextUnit.Day(this.toLong()))
val Int.weeks get() = UnitBasedTimeContainer(TimeContextUnit.Week(this.toLong()))
val Int.months get() = UnitBasedTimeContainer(TimeContextUnit.Month(this.toLong()))
val Int.years get() = UnitBasedTimeContainer(TimeContextUnit.Year(this.toLong()))