package bvanseg.kotlincommons.timedate

import bvanseg.kotlincommons.monads.Option
import bvanseg.kotlincommons.monads.none
import bvanseg.kotlincommons.monads.some
import bvanseg.kotlincommons.timedate.transformer.TimeTransformerContext

class TimePerformer(val inner: TimeContext): TimeContext by inner{
    internal var flag: TimePerformerFlag = TimePerformerFlag.PRONTO
    internal var startingCondition: Option<TimePerformerConditions.Starting> = none()
    internal var waitUntilCondition: Option<TimePerformerConditions.WaitUntil> = none()

    override val exactly: TimePerformer
        get(){
            flag = TimePerformerFlag.EXACTLY
            return this
        }

    override val pronto: TimePerformer
        get() = this

    override fun toString(): String = inner.toString()
}

class DefaultTimePerformer(val inner: TimeContext): TimeContext by inner, TimeTransformerContext {
    override val exactly: TimePerformer
        get(){
            val performer = TimePerformer(inner)
            return performer.exactly
        }

    override val pronto: TimePerformer
        get() = TimePerformer(this)
}

infix fun TimePerformer.waitUntil(context: TimeContainer): TimePerformer{
    this.waitUntilCondition = TimePerformerConditions.WaitUntil(context.pronto).some()
    return this
}

infix fun TimePerformer.waitUntil(context: TimePerformer): TimePerformer{
    this.waitUntilCondition = TimePerformerConditions.WaitUntil(context).some()
    return this
}

infix fun TimePerformer.starting(context: TimeContainer): TimePerformer{
    this.startingCondition = TimePerformerConditions.Starting(context.pronto).some()
    return this
}

infix fun TimePerformer.starting(context: TimePerformer): TimePerformer{
    this.startingCondition = TimePerformerConditions.Starting(context).some()
    return this
}

enum class TimePerformerFlag{
    EXACTLY,
    PRONTO
    ;
}

sealed class TimePerformerConditions(val start: TimePerformer){
    class WaitUntil(waitUntil: TimePerformer): TimePerformerConditions(waitUntil)
    class Starting(starting: TimePerformer): TimePerformerConditions(starting)
}