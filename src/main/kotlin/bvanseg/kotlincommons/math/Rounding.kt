/*
 * MIT License
 *
 * Copyright (c) 2021 Boston Vanseghi
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

import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor

/**
 * The rounding mode to be used in rounding-related functions.
 *
 * UP - Round up no matter what.
 * NEAREST - Automatically determine which number is closer to a given value and round to that number.
 * DOWN - Round down no matter what.
 *
 * @author Boston Vanseghi
 * @since 2.7.0
 */
enum class RoundingMode {
    UP,
    NEAREST,
    DOWN
}

/**
 * Rounds a given [value] to the nearest multiple of [nearest]. For example, given a value of 12.4 and a nearest value
 * of 2.5, 12.4 will be rounded to 12.5.
 *
 * @param value The value to round.
 * @param nearest The multiple to round the value to.
 * @param mode The [RoundingMode] to use in rounding calculations. Defaults to [RoundingMode.NEAREST].
 *
 * @return The rounded [value].
 *
 * @author Boston Vanseghi
 * @since 2.7.0
 */
fun roundToNearest(value: Double, nearest: Double, mode: RoundingMode = RoundingMode.NEAREST) = when (mode) {
    RoundingMode.UP -> nearest * (ceil(abs(value / nearest)))
    RoundingMode.NEAREST -> {
        val v = abs(value / nearest)
        val high = ceil(abs(value / nearest))
        val low = floor(abs(value / nearest))
        val highDiff = high - v
        val lowDiff = v - low

        when {
            highDiff > lowDiff -> low
            lowDiff > highDiff -> high
            else -> v
        } * nearest
    }
    RoundingMode.DOWN -> nearest * (floor(abs(value / nearest)))
}

fun Number.roundToNearest(nearest: Double, mode: RoundingMode = RoundingMode.NEAREST) =
    roundToNearest(this.toDouble(), nearest, mode)