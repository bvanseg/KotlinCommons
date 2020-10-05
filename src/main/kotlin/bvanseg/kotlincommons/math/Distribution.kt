/*
 * MIT License
 *
 * Copyright (c) 2020 Boston Vanseghi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package bvanseg.kotlincommons.math

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