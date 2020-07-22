package bvanseg.kotlincommons.timedate.transformer

import bvanseg.kotlincommons.timedate.*

infix fun TimeContext.without(unit: TimeUnit): TimeContext =
    when(this){
        is BoundedContext -> {
            left without unit
            right without unit
            this
        }
        is TimeCoercionContext -> coercedTime without unit
        is TimeContainer ->
            flatmap { it: Time ->
                when(unit){
                    TimeUnit.NANO -> Time(it.year, it.month, it.day, it.hour, it.minute, it.second, it.millis, 0)
                    TimeUnit.MILLIS -> Time(it.year, it.month, it.day, it.hour, it.minute, it.second, 0, it.nano)
                    TimeUnit.SECOND -> Time(it.year, it.month, it.day, it.hour, it.minute, 0, it.millis, it.nano)
                    TimeUnit.MINUTE -> Time(it.year, it.month, it.day, it.hour, 0, it.second, it.millis, it.nano)
                    TimeUnit.HOUR -> Time(it.year, it.month, it.day, 0, it.hour, it.second, it.millis, it.nano)
                    TimeUnit.DAY -> Time(it.year, it.month, 0, it.hour, it.hour, it.second, it.millis, it.nano)
                    TimeUnit.MONTH -> Time(it.year, 0, it.day, it.hour, it.hour, it.second, it.millis, it.nano)
                    TimeUnit.YEAR -> Time(0, it.month, it.day, it.hour, it.hour, it.second, it.millis, it.nano)
                }
            }
        else -> TODO("Uh not sure what you did but I like it ;)")
    }

infix fun TimeContext.truncate(unit: TimeUnit): TimeContext =
    when(this){
        is BoundedContext -> {
            val newLeft = left truncate unit
            val newRight = right truncate unit
            UntilContext(newLeft, newRight)
        }
        is TimeCoercionContext -> coercedTime truncate unit
        is TimeContainer -> {
            val here = if (this is LocalDateTimeContainer) this.toUnitBasedTimeContainer() else this
            here.flatmap { it: Time ->
                when (unit) {
                    TimeUnit.NANO -> Time(it.year, it.month, it.day, it.hour, it.minute, it.second, 0, 0)
                    TimeUnit.MILLIS -> Time(it.year, it.month, it.day, it.hour, it.minute, it.second, 0, 0)
                    TimeUnit.SECOND -> Time(it.year, it.month, it.day, it.hour, it.minute, 0, 0, 0)
                    TimeUnit.MINUTE -> Time(it.year, it.month, it.day, it.hour, 0, 0, 0, 0)
                    TimeUnit.HOUR -> Time(it.year, it.month, it.day, 0, 0, 0, 0, 0)
                    TimeUnit.DAY -> Time(it.year, it.month, 0, 0, 0, 0, 0, 0)
                    TimeUnit.MONTH -> Time(it.year, 0, 0, 0, 0, 0, 0, 0)
                    TimeUnit.YEAR -> Time(0, 0, 0, 0, 0, 0, 0, 0)
                }
            }
        }
        is TimePerformer -> inner truncate unit
        else -> TODO("Uh not sure what you did but I like it ;)")
    }
/*
    now truncate (hour > second)
 */