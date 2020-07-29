package bvanseg.kotlincommons.timedate

import bvanseg.kotlincommons.fp.dataclasses.Option
import bvanseg.kotlincommons.fp.dataclasses.none
import bvanseg.kotlincommons.fp.dataclasses.some
import bvanseg.kotlincommons.timedate.transformer.TimeTransformerContext
import bvanseg.kotlincommons.timedate.transformer.until

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

infix fun TimePerformer.at(context: TimeContainer): TimePerformer{
    this.waitUntilCondition = TimePerformerConditions.WaitUntil(context.pronto).some()
    return this
}

infix fun TimePerformer.at(context: TimePerformer): TimePerformer{
    this.waitUntilCondition = TimePerformerConditions.WaitUntil(context).some()
    return this
}

infix fun TimePerformer.at(context: TimeContextUnit): TimePerformer{
    this.waitUntilCondition = TimePerformerConditions.WaitUntil(context.pronto).some()
    return this
}

@Deprecated("Use `at` instead")
infix fun TimePerformer.waitUntil(context: TimeContainer): TimePerformer{
    this.waitUntilCondition = TimePerformerConditions.WaitUntil(context.pronto).some()
    return this
}

@Deprecated("Use `at` instead")
infix fun TimePerformer.waitUntil(context: TimePerformer): TimePerformer{
    this.waitUntilCondition = TimePerformerConditions.WaitUntil(context).some()
    return this
}

@Deprecated("Use `at` instead")
infix fun TimePerformer.waitUntil(context: TimeContextUnit): TimePerformer{
    val ctx = now until context
    this.startingCondition = TimePerformerConditions.Starting(ctx.pronto).some()
    return this
}

infix fun TimePerformer.starting(context: TimeContainer): TimePerformer{
    val ctx = now until context
    this.startingCondition = TimePerformerConditions.Starting(ctx.pronto).some()
    return this
}

infix fun TimePerformer.starting(context: TimeContextUnit): TimePerformer{
    val ctx = now until context
    this.startingCondition = TimePerformerConditions.Starting(ctx.pronto).some()
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
    @Deprecated("Deprecated in favor of At")
    class WaitUntil(waitUntil: TimePerformer): TimePerformerConditions(waitUntil)
    class At(at: TimePerformer): TimePerformerConditions(at)
    class Starting(starting: TimePerformer): TimePerformerConditions(starting)
}