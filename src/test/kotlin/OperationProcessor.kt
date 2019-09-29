import bvanseg.utilities.string.UTFBuffer

class OperationProcessor {

    fun process(string: String): Double {
        val buffer = UTFBuffer.wrap(string)

        // 1 + 1
        // -1 - 1

        var lastLeftSide: Double? = null
        while(buffer.isNotEmpty() && buffer.uptoWithSelector("+", "-", "*", "/", "%").second != " ") {

            val preOp = Op.from(buffer.toString()[0].toString())

            if(Op.from(buffer.toString()[0].toString()) != Op.NULL && lastLeftSide != null) {
                val num = buffer.nextNumber()?.toDouble() ?: 0.0
                lastLeftSide = preOp.mod(lastLeftSide, num)
            }else {
                val leftSide = buffer.nextNumber()?.toDouble() ?: 0.0
                if(lastLeftSide == null) lastLeftSide = leftSide

                val data = buffer.uptoWithSelector("+", "-", "*", "/", "%")
                val selector = data.second

                val rightSide = buffer.nextNumber()?.toDouble() ?: 0.0

                val operation = Op.from(selector)

                lastLeftSide = operation.mod(lastLeftSide, rightSide)
            }
        }

        return lastLeftSide!!
    }

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
}