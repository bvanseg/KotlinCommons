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