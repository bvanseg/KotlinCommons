package bvanseg.kotlincommons.time.api

import bvanseg.kotlincommons.project.Experimental

@Experimental
val Number.nanoseconds
    get() = KTime(this.toDouble(), KTimeUnit.NANOSECOND)

@Experimental
val Number.microseconds
    get() = KTime(this.toDouble(), KTimeUnit.MICROSECOND)

@Experimental
val Number.milliseconds
    get() = KTime(this.toDouble(), KTimeUnit.MILLISECOND)

@Experimental
val Number.seconds
    get() = KTime(this.toDouble(), KTimeUnit.SECOND)

@Experimental
val Number.minutes
    get() = KTime(this.toDouble(), KTimeUnit.MINUTE)

@Experimental
val Number.hours
    get() = KTime(this.toDouble(), KTimeUnit.HOUR)

@Experimental
val Number.days
    get() = KTime(this.toDouble(), KTimeUnit.DAY)

@Experimental
val Number.weeks
    get() = KTime(this.toDouble(), KTimeUnit.WEEK)

@Experimental
val Number.years
    get() = KTime(this.toDouble(), KTimeUnit.YEAR)

@Experimental
val Number.decades
    get() = KTime(this.toDouble(), KTimeUnit.DECADE)

@Experimental
val Number.centuries
    get() = KTime(this.toDouble(), KTimeUnit.CENTURY)

@Experimental
val Number.millenniums
    get() = KTime(this.toDouble(), KTimeUnit.MILLENNIUM)

@Experimental
val nanoseconds: KTimeBase = KTimeBase(KTimeUnit.NANOSECOND)
@Experimental
val microseconds: KTimeBase = KTimeBase(KTimeUnit.MICROSECOND)
@Experimental
val milliseconds: KTimeBase = KTimeBase(KTimeUnit.MILLISECOND)
@Experimental
val seconds: KTimeBase = KTimeBase(KTimeUnit.SECOND)
@Experimental
val minutes: KTimeBase = KTimeBase(KTimeUnit.MINUTE)
@Experimental
val hours: KTimeBase = KTimeBase(KTimeUnit.HOUR)
@Experimental
val days: KTimeBase = KTimeBase(KTimeUnit.DAY)
@Experimental
val weeks: KTimeBase = KTimeBase(KTimeUnit.WEEK)
@Experimental
val years: KTimeBase = KTimeBase(KTimeUnit.YEAR)
@Experimental
val decades: KTimeBase = KTimeBase(KTimeUnit.DECADE)
@Experimental
val centuries: KTimeBase = KTimeBase(KTimeUnit.CENTURY)
@Experimental
val millenniums: KTimeBase = KTimeBase(KTimeUnit.MILLENNIUM)

@Experimental
infix fun KTime.into(other: KTimeBase): KTime {
    val newValue = this.unit.convertTo(this.value, other.unit)
    return KTime(newValue, other.unit)
}

@Experimental
fun every(frequency: KTime, counterDrift: Boolean = false, cb: (KTimePerformer) -> Unit): KTimePerformer =
    KTimePerformer(frequency, cb, counterDrift)

@Experimental
operator fun KTime.compareTo(other: Number): Int {
    val otherValue = other.toDouble()
    return when {
        this.value == otherValue -> 0
        this.value > otherValue -> 1
        this.value < otherValue -> -1
        else -> -1
    }
}

@Experimental
operator fun Number.compareTo(other: KTime): Int {
    val thisValue = this.toDouble()
    return when {
        thisValue == other.value -> 0
        thisValue > other.value -> 1
        thisValue < other.value -> -1
        else -> -1
    }
}

@Experimental
operator fun KTime.compareTo(other: KTime): Int {
    val equalUnitValue = this.convertTo(other.unit)
    return when {
        equalUnitValue == other.value -> 0
        equalUnitValue > other.value -> 1
        equalUnitValue < other.value -> -1
        else -> -1
    }
}

@Experimental
operator fun KTime.rangeTo(other: KTime): KTime = other - this

@Experimental
operator fun KTime.dec(): KTime = this.apply { this.value -= 1 }
@Experimental
operator fun KTime.inc(): KTime = this.apply { this.value += 1 }

@Experimental
operator fun KTime.plus(other: Number): KTime = this.apply { this.value += other.toDouble() }
@Experimental
operator fun KTime.minus(other: Number): KTime = this.apply { this.value -= other.toDouble() }

@Experimental
operator fun KTime.plusAssign(other: Number) {
    this.apply { this.value += other.toDouble() }
}

@Experimental
operator fun KTime.minusAssign(other: Number) {
    this.apply { this.value -= other.toDouble() }
}

@Experimental
operator fun KTime.plus(other: KTime): KTime {
    val comparable = this.unit.compareTo(other.unit)

    return when {
        // This context has the greater time unit. Convert down to other.
        comparable > 0 -> {
            KTime(this.convertTo(other.unit) + other.value, other.unit)
        }
        comparable < 0 -> {
            KTime(other.convertTo(this.unit) + this.value, this.unit)
        }
        else -> {
            KTime(this.value + other.value, this.unit)
        }
    }
}

@Experimental
operator fun KTime.minus(other: KTime): KTime {
    val comparable = this.unit.compareTo(other.unit)

    return when {
        // This context has the greater time unit. Convert down to other.
        comparable > 0 -> {
            KTime(this.convertTo(other.unit) - other.value, other.unit)
        }
        comparable < 0 -> {
            KTime(other.convertTo(this.unit) - this.value, this.unit)
        }
        else -> {
            KTime(this.value - other.value, this.unit)
        }
    }
}

@Experimental
operator fun KTime.minusAssign(other: KTime) {
    val comparable = this.unit.compareTo(other.unit)

    when {
        // This context has the greater time unit. Convert down to other.
        comparable > 0 -> {
            this.value = this.convertTo(other.unit) - other.value
            this.unit = other.unit
        }
        comparable < 0 -> {
            this.value = other.convertTo(this.unit) - this.value
        }
        else -> {
            this.value = this.value - other.value
        }
    }
}