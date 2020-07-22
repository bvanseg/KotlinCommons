package bvanseg.kotlincommons.timedate.transformer

import bvanseg.kotlincommons.prettyprinter.buildPrettyString
import bvanseg.kotlincommons.timedate.*

interface BoundedContext: TimeTransformerContext {
    /**
     * The left side of the bounds. This is the 'lower' bounds.
     *
     * Examples:
     * ```
     * now until 10.minutes
     * ```
     * *now* is 'left'
     */
    val left: TimeContext

    /**
     * The right side of the bounds. This is the 'upper' bounds.
     * ```
     * now until 10.minutes
     * ```
     * *10.minutes* is 'right'
     */
    val right: TimeContext

}

class UntilContext(
    override val left: TimeContext,
    override val right: TimeContext
): BoundedContext,
    TimeContext
    by DefaultTimePerformer(left) {
    override val asHour: Long
        get(){
            val leftTime = left.asHour
            val rightTime = right.asHour
            return rightTime - leftTime
        }

    override val asMinute: Long
        get(){
            val leftTime = left.asMinute
            val rightTime = right.asMinute
            return rightTime - leftTime
        }
    override val asSeconds: Long
        get(){
            val leftTime = left.asSeconds
            val rightTime = right.asSeconds
            return rightTime - leftTime
        }
    override val asNano: Long
        get(){
            val leftTime = left.asNano
            val rightTime = right.asNano
            return rightTime - leftTime
        }
    override val asMillis: Long = right.asMillis - left.asMillis
//        get(){
//            val leftTime = left.asMillis
//            val rightTime = right.asMillis
//            return rightTime - leftTime
//        }

    @ExperimentalStdlibApi
    override fun toString(): String =
        (right - left).toString()
}

infix fun TimeContext.until(context: TimeContext) = UntilContext(this, context)