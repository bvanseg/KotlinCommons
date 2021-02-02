package bvanseg.kotlincommons.time.api

val Number.nanoseconds
    get() = Khrono(this.toDouble(), KhronoUnit.NANOSECOND)

val Number.microseconds
    get() = Khrono(this.toDouble(), KhronoUnit.MICROSECOND)

val Number.milliseconds
    get() = Khrono(this.toDouble(), KhronoUnit.MILLISECOND)

val Number.seconds
    get() = Khrono(this.toDouble(), KhronoUnit.SECOND)

val Number.minutes
    get() = Khrono(this.toDouble(), KhronoUnit.MINUTE)

val Number.hours
    get() = Khrono(this.toDouble(), KhronoUnit.HOUR)

val Number.days
    get() = Khrono(this.toDouble(), KhronoUnit.DAY)

val Number.weeks
    get() = Khrono(this.toDouble(), KhronoUnit.WEEK)

val Number.years
    get() = Khrono(this.toDouble(), KhronoUnit.YEAR)

val Number.decades
    get() = Khrono(this.toDouble(), KhronoUnit.DECADE)

val Number.centuries
    get() = Khrono(this.toDouble(), KhronoUnit.CENTURY)

val Number.millenniums
    get() = Khrono(this.toDouble(), KhronoUnit.MILLENNIUM)

val nanoseconds: KhronoUnit = KhronoUnit.NANOSECOND
val microseconds: KhronoUnit = KhronoUnit.MICROSECOND
val milliseconds: KhronoUnit = KhronoUnit.MILLISECOND
val seconds: KhronoUnit = KhronoUnit.SECOND
val minutes: KhronoUnit = KhronoUnit.MINUTE
val hours: KhronoUnit = KhronoUnit.HOUR
val days: KhronoUnit = KhronoUnit.DAY
val halfDays: KhronoUnit = KhronoUnit.HALF_DAY
val weeks: KhronoUnit = KhronoUnit.WEEK
val years: KhronoUnit = KhronoUnit.YEAR
val decades: KhronoUnit = KhronoUnit.DECADE
val centuries: KhronoUnit = KhronoUnit.CENTURY
val millenniums: KhronoUnit = KhronoUnit.MILLENNIUM

infix fun Khrono.into(unit: KhronoUnit): Khrono {
    val newValue = this.unit.convertTo(this.value, unit)
    return Khrono(newValue, unit)
}

fun every(timeInMillis: Long, counterDrift: Boolean = false, cb: (KhronoPerformer) -> Unit): KhronoPerformer =
    every(timeInMillis.milliseconds, counterDrift, cb)

fun every(frequency: Khrono, counterDrift: Boolean = false, cb: (KhronoPerformer) -> Unit): KhronoPerformer =
    KhronoPerformer(frequency, cb, counterDrift)

operator fun Khrono.compareTo(other: Number): Int {
    val otherValue = other.toDouble()
    return when {
        this.value == otherValue -> 0
        this.value > otherValue -> 1
        this.value < otherValue -> -1
        else -> -1
    }
}

operator fun Number.compareTo(other: Khrono): Int {
    val thisValue = this.toDouble()
    return when {
        thisValue == other.value -> 0
        thisValue > other.value -> 1
        thisValue < other.value -> -1
        else -> -1
    }
}

operator fun Khrono.compareTo(other: Khrono): Int {
    val equalUnitValue = this.convertTo(other.unit)
    return when {
        equalUnitValue == other.value -> 0
        equalUnitValue > other.value -> 1
        equalUnitValue < other.value -> -1
        else -> -1
    }
}

operator fun Khrono.rangeTo(other: Khrono): Khrono = other - this

operator fun Khrono.dec(): Khrono = this.apply { this.value -= 1 }

operator fun Khrono.inc(): Khrono = this.apply { this.value += 1 }

operator fun Khrono.plus(other: Number): Khrono = this.apply { this.value += other.toDouble() }

operator fun Khrono.minus(other: Number): Khrono = this.apply { this.value -= other.toDouble() }

operator fun Khrono.plusAssign(other: Number) {
    this.apply { this.value += other.toDouble() }
}

operator fun Khrono.minusAssign(other: Number) {
    this.apply { this.value -= other.toDouble() }
}

operator fun Khrono.plus(other: Khrono): Khrono {
    val comparable = this.unit.compareTo(other.unit)

    return when {
        // This context has the greater time unit. Convert down to other.
        comparable > 0 -> {
            Khrono(this.convertTo(other.unit) + other.value, other.unit)
        }
        comparable < 0 -> {
            Khrono(other.convertTo(this.unit) + this.value, this.unit)
        }
        else -> {
            Khrono(this.value + other.value, this.unit)
        }
    }
}

operator fun Khrono.minus(other: Khrono): Khrono {
    val comparable = this.unit.compareTo(other.unit)

    return when {
        // This context has the greater time unit. Convert down to other.
        comparable > 0 -> {
            Khrono(this.convertTo(other.unit) - other.value, other.unit)
        }
        comparable < 0 -> {
            Khrono(other.convertTo(this.unit) - this.value, this.unit)
        }
        else -> {
            Khrono(this.value - other.value, this.unit)
        }
    }
}

operator fun Khrono.minusAssign(other: Khrono) {
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