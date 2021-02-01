package bvanseg.kotlincommons.time.api

val Number.nanoseconds
    get() = KTime(this.toDouble(), KTimeUnit.NANOSECOND)

val Number.microseconds
    get() = KTime(this.toDouble(), KTimeUnit.MICROSECOND)

val Number.milliseconds
    get() = KTime(this.toDouble(), KTimeUnit.MILLISECOND)

val Number.seconds
    get() = KTime(this.toDouble(), KTimeUnit.SECOND)

val Number.minutes
    get() = KTime(this.toDouble(), KTimeUnit.MINUTE)

val Number.hours
    get() = KTime(this.toDouble(), KTimeUnit.HOUR)

val Number.days
    get() = KTime(this.toDouble(), KTimeUnit.DAY)

val Number.weeks
    get() = KTime(this.toDouble(), KTimeUnit.WEEK)

val Number.years
    get() = KTime(this.toDouble(), KTimeUnit.YEAR)

val Number.decades
    get() = KTime(this.toDouble(), KTimeUnit.DECADE)

val Number.centuries
    get() = KTime(this.toDouble(), KTimeUnit.CENTURY)

val Number.millenniums
    get() = KTime(this.toDouble(), KTimeUnit.MILLENNIUM)

val nanoseconds: KTimeUnit = KTimeUnit.NANOSECOND
val microseconds: KTimeUnit = KTimeUnit.MICROSECOND
val milliseconds: KTimeUnit = KTimeUnit.MILLISECOND
val seconds: KTimeUnit = KTimeUnit.SECOND
val minutes: KTimeUnit = KTimeUnit.MINUTE
val hours: KTimeUnit = KTimeUnit.HOUR
val days: KTimeUnit = KTimeUnit.DAY
val halfDays: KTimeUnit = KTimeUnit.HALF_DAY
val weeks: KTimeUnit = KTimeUnit.WEEK
val years: KTimeUnit = KTimeUnit.YEAR
val decades: KTimeUnit = KTimeUnit.DECADE
val centuries: KTimeUnit = KTimeUnit.CENTURY
val millenniums: KTimeUnit = KTimeUnit.MILLENNIUM

infix fun KTime.into(unit: KTimeUnit): KTime {
    val newValue = this.unit.convertTo(this.value, unit)
    return KTime(newValue, unit)
}

fun every(frequency: KTime, counterDrift: Boolean = false, cb: (KTimePerformer) -> Unit): KTimePerformer =
    KTimePerformer(frequency, cb, counterDrift)

operator fun KTime.compareTo(other: Number): Int {
    val otherValue = other.toDouble()
    return when {
        this.value == otherValue -> 0
        this.value > otherValue -> 1
        this.value < otherValue -> -1
        else -> -1
    }
}

operator fun Number.compareTo(other: KTime): Int {
    val thisValue = this.toDouble()
    return when {
        thisValue == other.value -> 0
        thisValue > other.value -> 1
        thisValue < other.value -> -1
        else -> -1
    }
}

operator fun KTime.compareTo(other: KTime): Int {
    val equalUnitValue = this.convertTo(other.unit)
    return when {
        equalUnitValue == other.value -> 0
        equalUnitValue > other.value -> 1
        equalUnitValue < other.value -> -1
        else -> -1
    }
}

operator fun KTime.rangeTo(other: KTime): KTime = other - this

operator fun KTime.dec(): KTime = this.apply { this.value -= 1 }

operator fun KTime.inc(): KTime = this.apply { this.value += 1 }

operator fun KTime.plus(other: Number): KTime = this.apply { this.value += other.toDouble() }

operator fun KTime.minus(other: Number): KTime = this.apply { this.value -= other.toDouble() }

operator fun KTime.plusAssign(other: Number) {
    this.apply { this.value += other.toDouble() }
}

operator fun KTime.minusAssign(other: Number) {
    this.apply { this.value -= other.toDouble() }
}

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