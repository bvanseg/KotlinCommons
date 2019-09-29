package kcommons.utilities.numbers

enum class Op {
    ADD {
        override fun mod(p1: Double, p2: Double): Double = p1 + p2
    },
    SUBTRACT {
        override fun mod(p1: Double, p2: Double): Double = p1 - p2
    },
    MULTIPLY {
        override fun mod(p1: Double, p2: Double): Double = p1 * p2
    },
    DIVIDE {
        override fun mod(p1: Double, p2: Double): Double = p1 / p2
    },
    MODULO {
        override fun mod(p1: Double, p2: Double): Double = p1 % p2
    },
    NULL {
        override fun mod(p1: Double, p2: Double): Double {
            return -1.0
        }
    };

    abstract fun mod(p1: Double, p2: Double): Double

    companion object {

        @JvmStatic
        fun from(operator: String): Op =
            when(operator) {
                "+" -> ADD
                "-" -> SUBTRACT
                "*" -> MULTIPLY
                "/" -> DIVIDE
                "%" -> MODULO
                else -> NULL
            }
    }
}