package bvanseg.kotlincommons.time.api

abstract class KTimeTransformer {
    abstract fun transform(value: Double, unit: KTimeUnit): Double
}

object NanosecondTransformer: KTimeTransformer() {
    override fun transform(value: Double, unit: KTimeUnit): Double {
        return when(unit) {
            KTimeUnit.NANOSECOND -> value
            KTimeUnit.MICROSECOND -> value / 1_000.0
            KTimeUnit.MILLISECOND -> value / 1_000_000.0
            KTimeUnit.SECOND -> value / (1_000_000.0 * 1000)
            KTimeUnit.MINUTE -> value / (1_000_000.0 * 1000 * 60)
            KTimeUnit.HOUR -> value / (1_000_000.0 * 1000 * 60 * 60)
            KTimeUnit.DAY -> value / (1_000_000.0 * 1000 * 60 * 60 * 24)
            KTimeUnit.WEEK -> value / (1_000_000.0 * 1000 * 60 * 60 * 24 * 7)
            KTimeUnit.YEAR -> value / (1_000_000.0 * 1000 * 60 * 60 * 24 * 365)
            KTimeUnit.DECADE -> value / (1_000_000.0 * 1000 * 60 * 60 * 24 * 365 * 10)
            KTimeUnit.CENTURY -> value / (1_000_000.0 * 1000 * 60 * 60 * 24 * 365 * 100)
            KTimeUnit.MILLENNIUM -> value / (1_000_000.0 * 1000 * 60 * 60 * 24 * 365 * 1000)
        }
    }
}

object MicrosecondTransformer: KTimeTransformer() {
    override fun transform(value: Double, unit: KTimeUnit): Double {
        return when(unit) {
            KTimeUnit.NANOSECOND -> value * 1_000.0
            KTimeUnit.MICROSECOND -> value
            KTimeUnit.MILLISECOND -> value / 1_000.0
            KTimeUnit.SECOND -> value / (1_000.0 * 1000)
            KTimeUnit.MINUTE -> value / (1_000.0 * 1000 * 60)
            KTimeUnit.HOUR -> value / (1_000.0 * 1000 * 60 * 60)
            KTimeUnit.DAY -> value / (1_000.0 * 1000 * 60 * 60 * 24)
            KTimeUnit.WEEK -> value / (1_000.0 * 1000 * 60 * 60 * 24 * 7)
            KTimeUnit.YEAR -> value / (1_000.0 * 1000 * 60 * 60 * 24 * 365)
            KTimeUnit.DECADE -> value / (1_000.0 * 1000 * 60 * 60 * 24 * 365 * 10)
            KTimeUnit.CENTURY -> value / (1_000.0 * 1000 * 60 * 60 * 24 * 365 * 100)
            KTimeUnit.MILLENNIUM -> value / (1_000.0 * 1000 * 60 * 60 * 24 * 365 * 1000)
        }
    }
}

object MillisecondTransformer: KTimeTransformer() {
    override fun transform(value: Double, unit: KTimeUnit): Double {
        return when(unit) {
            KTimeUnit.NANOSECOND -> value * 1_000_000.0
            KTimeUnit.MICROSECOND -> value * 1_000.0
            KTimeUnit.MILLISECOND -> value
            KTimeUnit.SECOND -> value / 1000.0
            KTimeUnit.MINUTE -> value / (1000.0 * 60)
            KTimeUnit.HOUR -> value / (1000.0 * 60 * 60)
            KTimeUnit.DAY -> value / (1000.0 * 60 * 60 * 24)
            KTimeUnit.WEEK -> value / (1000.0 * 60 * 60 * 24 * 7)
            KTimeUnit.YEAR -> value / (1000.0 * 60 * 60 * 24 * 365)
            KTimeUnit.DECADE -> value /( 1000.0 * 60 * 60 * 24 * 365 * 10)
            KTimeUnit.CENTURY -> value / (1000.0 * 60 * 60 * 24 * 365 * 100)
            KTimeUnit.MILLENNIUM -> value / (1000.0 * 60 * 60 * 24 * 365 * 1000)
        }
    }
}

object SecondTransformer: KTimeTransformer() {
    override fun transform(value: Double, unit: KTimeUnit): Double {
        return when(unit) {
            KTimeUnit.NANOSECOND -> value * 1_000_000.0 * 1000
            KTimeUnit.MICROSECOND -> value * 1_000.0 * 1000
            KTimeUnit.MILLISECOND -> value * 1000.0
            KTimeUnit.SECOND -> value
            KTimeUnit.MINUTE -> value / 60.0
            KTimeUnit.HOUR -> value / (60.0 * 60)
            KTimeUnit.DAY -> value / (60.0 * 60 * 24)
            KTimeUnit.WEEK -> value / (60.0 * 60 * 24 * 7)
            KTimeUnit.YEAR -> value / (60.0 * 60 * 24 * 365)
            KTimeUnit.DECADE -> value / (60.0 * 60 * 24 * 365 * 10)
            KTimeUnit.CENTURY -> value / (60.0 * 60 * 24 * 365 * 100)
            KTimeUnit.MILLENNIUM -> value / (60.0 * 60 * 24 * 365 * 1000)
        }
    }
}

object MinuteTransformer: KTimeTransformer() {
    override fun transform(value: Double, unit: KTimeUnit): Double {
        return when(unit) {
            KTimeUnit.NANOSECOND -> value * 1_000_000.0 * 1000 * 60
            KTimeUnit.MICROSECOND -> value * 1_000.0 * 1000 * 60
            KTimeUnit.MILLISECOND -> value * 1000.0 * 60
            KTimeUnit.SECOND -> value * 60.0
            KTimeUnit.MINUTE -> value
            KTimeUnit.HOUR -> value / 60.0
            KTimeUnit.DAY -> value / (60.0 * 24)
            KTimeUnit.WEEK -> value / (60.0 * 24 * 7)
            KTimeUnit.YEAR -> value / (60.0 * 24 * 365)
            KTimeUnit.DECADE -> value / (60.0 * 24 * 365 * 10)
            KTimeUnit.CENTURY -> value / (60.0 * 24 * 365 * 100)
            KTimeUnit.MILLENNIUM -> value / (60.0 * 24 * 365 * 1000)
        }
    }
}

object HourTransformer: KTimeTransformer() {
    override fun transform(value: Double, unit: KTimeUnit): Double {
        return when(unit) {
            KTimeUnit.NANOSECOND -> value * 1_000_000.0 * 1000 * 60 * 60
            KTimeUnit.MICROSECOND -> value * 1_000.0 * 1000 * 60 * 60
            KTimeUnit.MILLISECOND -> value * 1000.0 * 60 * 60
            KTimeUnit.SECOND -> value * 60.0 * 60
            KTimeUnit.MINUTE -> value * 60.0
            KTimeUnit.HOUR -> value
            KTimeUnit.DAY -> value / 24.0
            KTimeUnit.WEEK -> value / (24.0 * 7)
            KTimeUnit.YEAR -> value / (24.0 * 365)
            KTimeUnit.DECADE -> value / (24.0 * 365 * 10)
            KTimeUnit.CENTURY -> value / (24.0 * 365 * 100)
            KTimeUnit.MILLENNIUM -> value / (24.0 * 365 * 1000)
        }
    }
}

object DayTransformer: KTimeTransformer() {
    override fun transform(value: Double, unit: KTimeUnit): Double {
        return when(unit) {
            KTimeUnit.NANOSECOND -> value * 1_000_000.0 * 1000 * 60 * 60 * 24
            KTimeUnit.MICROSECOND -> value * 1_000.0 * 1000 * 60 * 60 * 24
            KTimeUnit.MILLISECOND -> value * 1000.0 * 60 * 60 * 24
            KTimeUnit.SECOND -> value * 60.0 * 60 * 24
            KTimeUnit.MINUTE -> value * 60.0 * 24
            KTimeUnit.HOUR -> value * 24
            KTimeUnit.DAY -> value
            KTimeUnit.WEEK -> value / 7.0
            KTimeUnit.YEAR -> value / 365.0
            KTimeUnit.DECADE -> value / (365.0 * 10)
            KTimeUnit.CENTURY -> value / (365.0 * 100)
            KTimeUnit.MILLENNIUM -> value / (365.0 * 1000)
        }
    }
}

object WeekTransformer: KTimeTransformer() {
    override fun transform(value: Double, unit: KTimeUnit): Double {
        return when(unit) {
            KTimeUnit.NANOSECOND -> value * 1_000_000.0 * 1000 * 60 * 60 * 24 * 7
            KTimeUnit.MICROSECOND -> value * 1_000.0 * 1000 * 60 * 60 * 24 * 7
            KTimeUnit.MILLISECOND -> value * 1000.0 * 60 * 60 * 24 * 7
            KTimeUnit.SECOND -> value * 60.0 * 60 * 24 * 7
            KTimeUnit.MINUTE -> value * 60.0 * 24 * 7
            KTimeUnit.HOUR -> value * 24 * 7
            KTimeUnit.DAY -> value * 7
            KTimeUnit.WEEK -> value
            KTimeUnit.YEAR -> (value * 7) / 365.0
            KTimeUnit.DECADE -> (value * 7) / (365.0 * 10)
            KTimeUnit.CENTURY -> (value * 7) / (365.0 * 100)
            KTimeUnit.MILLENNIUM -> (value * 7) / (365.0 * 1000)
        }
    }
}

object YearTransformer: KTimeTransformer() {
    override fun transform(value: Double, unit: KTimeUnit): Double {
        return when(unit) {
            KTimeUnit.NANOSECOND -> value * 1_000_000.0 * 1000 * 60 * 60 * 24 * 365
            KTimeUnit.MICROSECOND -> value * 1_000.0 * 1000 * 60 * 60 * 24 * 365
            KTimeUnit.MILLISECOND -> value * 1000.0 * 60 * 60 * 24 * 365
            KTimeUnit.SECOND -> value * 60.0 * 60 * 24 * 365
            KTimeUnit.MINUTE -> value * 60.0 * 24 * 365
            KTimeUnit.HOUR -> value * 24 * 365
            KTimeUnit.DAY -> value * 365
            KTimeUnit.WEEK -> (value * 365) / 7.0
            KTimeUnit.YEAR -> value
            KTimeUnit.DECADE -> value / 10.0
            KTimeUnit.CENTURY -> value / 100.0
            KTimeUnit.MILLENNIUM -> value / 1000.0
        }
    }
}

object DecadeTransformer: KTimeTransformer() {
    override fun transform(value: Double, unit: KTimeUnit): Double {
        return when(unit) {
            KTimeUnit.NANOSECOND -> value * 1_000_000.0 * 1000 * 60 * 60 * 24 * 365 * 10
            KTimeUnit.MICROSECOND -> value * 1_000.0 * 1000 * 60 * 60 * 24 * 365 * 10
            KTimeUnit.MILLISECOND -> value * 1000.0 * 60 * 60 * 24 * 365 * 10
            KTimeUnit.SECOND -> value * 60.0 * 60 * 24 * 365 * 10
            KTimeUnit.MINUTE -> value * 60.0 * 24 * 365 * 10
            KTimeUnit.HOUR -> value * 24 * 365 * 10
            KTimeUnit.DAY -> value * 365 * 10
            KTimeUnit.WEEK -> (value * 365 * 10) / 7.0
            KTimeUnit.YEAR -> value * 10
            KTimeUnit.DECADE -> value
            KTimeUnit.CENTURY -> value / 10.0
            KTimeUnit.MILLENNIUM -> value / 100.0
        }
    }
}

object CenturyTransformer: KTimeTransformer() {
    override fun transform(value: Double, unit: KTimeUnit): Double {
        return when(unit) {
            KTimeUnit.NANOSECOND -> value * 1_000_000.0 * 1000 * 60 * 60 * 24 * 365 * 100
            KTimeUnit.MICROSECOND -> value * 1_000.0 * 1000 * 60 * 60 * 24 * 365 * 100
            KTimeUnit.MILLISECOND -> value * 1000.0 * 60 * 60 * 24 * 365 * 100
            KTimeUnit.SECOND -> value * 60.0 * 60 * 24 * 365 * 100
            KTimeUnit.MINUTE -> value * 60.0 * 24 * 365 * 100
            KTimeUnit.HOUR -> value * 24 * 365 * 100
            KTimeUnit.DAY -> value * 365 * 100
            KTimeUnit.WEEK -> (value * 365 * 100) / 7.0
            KTimeUnit.YEAR -> value * 100
            KTimeUnit.DECADE -> value * 10
            KTimeUnit.CENTURY -> value
            KTimeUnit.MILLENNIUM -> value / 10.0
        }
    }
}

object MillenniumTransformer: KTimeTransformer() {
    override fun transform(value: Double, unit: KTimeUnit): Double {
        return when(unit) {
            KTimeUnit.NANOSECOND -> value * 1_000_000.0 * 1000 * 60 * 60 * 24 * 365 * 1000
            KTimeUnit.MICROSECOND -> value * 1_000.0 * 1000 * 60 * 60 * 24 * 365 * 1000
            KTimeUnit.MILLISECOND -> value * 1000.0 * 60 * 60 * 24 * 365 * 1000
            KTimeUnit.SECOND -> value * 60.0 * 60 * 24 * 365 * 1000
            KTimeUnit.MINUTE -> value * 60.0 * 24 * 365 * 1000
            KTimeUnit.HOUR -> value * 24 * 365 * 1000
            KTimeUnit.DAY -> value * 365 * 1000
            KTimeUnit.WEEK -> (value * 365 * 1000) / 7.0
            KTimeUnit.YEAR -> value * 1000
            KTimeUnit.DECADE -> value * 100
            KTimeUnit.CENTURY -> value * 10
            KTimeUnit.MILLENNIUM -> value
        }
    }
}