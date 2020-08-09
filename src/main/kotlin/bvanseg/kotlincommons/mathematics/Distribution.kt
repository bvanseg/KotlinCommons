package bvanseg.kotlincommons.mathematics

import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.sqrt

/**
 * @author Boston Vanseghi
 * @since 2.2.5
 */
fun normalPdf(x: Double): Double = exp((-x * x) / 2) / sqrt(2 * Math.PI)

/**
 * @author Boston Vanseghi
 * @since 2.2.5
 */
fun normalCdf(x: Double): Double {
    var temp = x
    val neg = if (temp < 0.0) 1 else 0
    if (neg == 1) temp *= -1.0
    val k = 1.0 / (1.0 + 0.2316419 * temp)
    var y =
        ((((1.330274429 * k - 1.821255978) * k + 1.781477937) * k - 0.356563782) * k + 0.319381530) *
                k
    y = 1.0 - 0.398942280401 * exp(-0.5 * temp * temp) * y
    return (1.0 - neg) * y + neg * (1.0 - y)
}