package bvanseg.kcommons.bytes

import kotlin.math.pow

enum class ByteUnit(private val power: Int) {
    B(0),
    KB(1),
    MB(2),
    GB(3),
    TB(4);

    fun getBytes(bytes: Int = 1): Int {
        return 1024.0.pow(power.toDouble()).toInt() * bytes
    }

    fun to(type: ByteUnit, count: Long = 1): Double {
        return 1024.0.pow(power) / 1024.0.pow(type.power) * count
    }
}