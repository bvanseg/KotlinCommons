package bvanseg.kotlincommons.timedate

import bvanseg.kotlincommons.timedate.transformer.until

operator fun UnitBasedTimeContainer.plus(context: TimeContainer) =
    when(context) {
        is UnitBasedTimeContainer -> {
            when(unit){
                is TimeContextUnit.Nano -> UnitBasedTimeContainer(TimeContextUnit.Nano(unit.asSeconds + context.asSeconds))
                is TimeContextUnit.Second -> UnitBasedTimeContainer(TimeContextUnit.Second(unit.asSeconds + context.asSeconds))
                is TimeContextUnit.Minute -> UnitBasedTimeContainer(TimeContextUnit.Minute(unit.asSeconds + context.asSeconds))
                is TimeContextUnit.Hour -> UnitBasedTimeContainer(TimeContextUnit.Hour(unit.asSeconds + context.asSeconds))
                is TimeContextUnit.Day -> UnitBasedTimeContainer(TimeContextUnit.Day(unit.asSeconds + context.asSeconds))
                is TimeContextUnit.Month -> UnitBasedTimeContainer(TimeContextUnit.Week(unit.asSeconds + context.asSeconds))
                is TimeContextUnit.Year -> UnitBasedTimeContainer(TimeContextUnit.Year(unit.asSeconds + context.asSeconds))
                else -> throw Exception("Failed to add times together!")
            }
        }
        is LocalDateTimeContainer ->
            when(unit){
                is TimeContextUnit.Nano -> UnitBasedTimeContainer(TimeContextUnit.Nano(unit.asSeconds + context.asSeconds))
                is TimeContextUnit.Second -> UnitBasedTimeContainer(TimeContextUnit.Second(unit.asSeconds + context.asSeconds))
                is TimeContextUnit.Minute -> UnitBasedTimeContainer(TimeContextUnit.Minute(unit.asSeconds + context.asSeconds))
                is TimeContextUnit.Hour -> UnitBasedTimeContainer(TimeContextUnit.Hour(unit.asSeconds + context.asSeconds))
                is TimeContextUnit.Day -> UnitBasedTimeContainer(TimeContextUnit.Day(unit.asSeconds + context.asSeconds))
                is TimeContextUnit.Month -> UnitBasedTimeContainer(TimeContextUnit.Week(unit.asSeconds + context.asSeconds))
                is TimeContextUnit.Year -> UnitBasedTimeContainer(TimeContextUnit.Year(unit.asSeconds + context.asSeconds))
                else -> throw Exception("Failed to add times together!")
            }
        else -> throw Exception("Failed to add times together!")
    }

operator fun UnitBasedTimeContainer.minus(context: TimeContainer) =
    when(context) {
        is UnitBasedTimeContainer -> {
            when(unit){
                is TimeContextUnit.Nano -> UnitBasedTimeContainer(TimeContextUnit.Nano(unit.asSeconds + context.asSeconds))
                is TimeContextUnit.Second -> UnitBasedTimeContainer(TimeContextUnit.Second(unit.asSeconds + context.asSeconds))
                is TimeContextUnit.Minute -> UnitBasedTimeContainer(TimeContextUnit.Minute(unit.asSeconds + context.asSeconds))
                is TimeContextUnit.Hour -> UnitBasedTimeContainer(TimeContextUnit.Hour(unit.asSeconds + context.asSeconds))
                is TimeContextUnit.Day -> UnitBasedTimeContainer(TimeContextUnit.Day(unit.asSeconds + context.asSeconds))
                is TimeContextUnit.Month -> UnitBasedTimeContainer(TimeContextUnit.Week(unit.asSeconds + context.asSeconds))
                is TimeContextUnit.Year -> UnitBasedTimeContainer(TimeContextUnit.Year(unit.asSeconds + context.asSeconds))
                else -> throw Exception("Failed to add times together!")
            }
        }
        is LocalDateTimeContainer ->
            when(unit){
                is TimeContextUnit.Nano -> UnitBasedTimeContainer(TimeContextUnit.Nano(unit.asSeconds + context.asSeconds))
                is TimeContextUnit.Second -> UnitBasedTimeContainer(TimeContextUnit.Second(unit.asSeconds + context.asSeconds))
                is TimeContextUnit.Minute -> UnitBasedTimeContainer(TimeContextUnit.Minute(unit.asSeconds + context.asSeconds))
                is TimeContextUnit.Hour -> UnitBasedTimeContainer(TimeContextUnit.Hour(unit.asSeconds + context.asSeconds))
                is TimeContextUnit.Day -> UnitBasedTimeContainer(TimeContextUnit.Day(unit.asSeconds + context.asSeconds))
                is TimeContextUnit.Month -> UnitBasedTimeContainer(TimeContextUnit.Week(unit.asSeconds + context.asSeconds))
                is TimeContextUnit.Year -> UnitBasedTimeContainer(TimeContextUnit.Year(unit.asSeconds + context.asSeconds))
                else -> throw Exception("Failed to add times together!")
            }
        else -> throw Exception("Failed to add times together!")
    }

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
operator fun LocalDateTimeContainer.plus(context: TimeContainer) =
    when(context) {
        is UnitBasedTimeContainer -> {
            when(context.unit){
                is TimeContextUnit.Nano -> UnitBasedTimeContainer(TimeContextUnit.Nano(asSeconds + context.asSeconds))
                is TimeContextUnit.Second -> UnitBasedTimeContainer(TimeContextUnit.Second(asSeconds + context.asSeconds))
                is TimeContextUnit.Minute -> UnitBasedTimeContainer(TimeContextUnit.Minute(asSeconds + context.asSeconds))
                is TimeContextUnit.Hour -> UnitBasedTimeContainer(TimeContextUnit.Hour(asSeconds + context.asSeconds))
                is TimeContextUnit.Day -> UnitBasedTimeContainer(TimeContextUnit.Day(asSeconds + context.asSeconds))
                is TimeContextUnit.Month -> UnitBasedTimeContainer(TimeContextUnit.Week(asSeconds + context.asSeconds))
                is TimeContextUnit.Year -> UnitBasedTimeContainer(TimeContextUnit.Year(asSeconds + context.asSeconds))
                else -> throw Exception("Failed to add times together!")
            }
        }
        is LocalDateTimeContainer -> TODO()
//            when(context.contextUnit){
//                is TimeContextUnit.Nano -> UnitBasedTimeContainer(TimeContextUnit.Nano(asSeconds + context.asSeconds))
//                is TimeContextUnit.Second -> UnitBasedTimeContainer(TimeContextUnit.Second(asSeconds + context.asSeconds))
//                is TimeContextUnit.Minute -> UnitBasedTimeContainer(TimeContextUnit.Minute(asSeconds + context.asSeconds))
//                is TimeContextUnit.Hour -> UnitBasedTimeContainer(TimeContextUnit.Hour(asSeconds + context.asSeconds))
//                is TimeContextUnit.Day -> UnitBasedTimeContainer(TimeContextUnit.Day(asSeconds + context.asSeconds))
//                is TimeContextUnit.Month -> UnitBasedTimeContainer(TimeContextUnit.Week(asSeconds + context.asSeconds))
//                is TimeContextUnit.Year -> UnitBasedTimeContainer(TimeContextUnit.Year(asSeconds + context.asSeconds))
//                else -> throw Exception("Failed to add times together!")
//            }
        else -> throw Exception("Failed to add times together!")
    }

operator fun LocalDateTimeContainer.minus(context: TimeContainer) =
    when(context) {
        is UnitBasedTimeContainer -> {
            when(context.unit){
                is TimeContextUnit.Nano -> UnitBasedTimeContainer(TimeContextUnit.Nano(asSeconds + context.asSeconds))
                is TimeContextUnit.Second -> UnitBasedTimeContainer(TimeContextUnit.Second(asSeconds + context.asSeconds))
                is TimeContextUnit.Minute -> UnitBasedTimeContainer(TimeContextUnit.Minute(asSeconds + context.asSeconds))
                is TimeContextUnit.Hour -> UnitBasedTimeContainer(TimeContextUnit.Hour(asSeconds + context.asSeconds))
                is TimeContextUnit.Day -> UnitBasedTimeContainer(TimeContextUnit.Day(asSeconds + context.asSeconds))
                is TimeContextUnit.Month -> UnitBasedTimeContainer(TimeContextUnit.Week(asSeconds + context.asSeconds))
                is TimeContextUnit.Year -> UnitBasedTimeContainer(TimeContextUnit.Year(asSeconds + context.asSeconds))
                else -> throw Exception("Failed to add times together!")
            }
        }
        is LocalDateTimeContainer -> TODO()
//            when(context){
//                is TimeContextUnit.Nano -> UnitBasedTimeContainer(TimeContextUnit.Nano(contextUnit.asSeconds + context.asSeconds))
//                is TimeContextUnit.Second -> UnitBasedTimeContainer(TimeContextUnit.Second(contextUnit.asSeconds + context.asSeconds))
//                is TimeContextUnit.Minute -> UnitBasedTimeContainer(TimeContextUnit.Minute(contextUnit.asSeconds + context.asSeconds))
//                is TimeContextUnit.Hour -> UnitBasedTimeContainer(TimeContextUnit.Hour(contextUnit.asSeconds + context.asSeconds))
//                is TimeContextUnit.Day -> UnitBasedTimeContainer(TimeContextUnit.Day(contextUnit.asSeconds + context.asSeconds))
//                is TimeContextUnit.Month -> UnitBasedTimeContainer(TimeContextUnit.Week(contextUnit.asSeconds + context.asSeconds))
//                is TimeContextUnit.Year -> UnitBasedTimeContainer(TimeContextUnit.Year(contextUnit.asSeconds + context.asSeconds))
//                else -> throw Exception("Failed to add times together!")
//            }
        else -> throw Exception("Failed to add times together!")
    }