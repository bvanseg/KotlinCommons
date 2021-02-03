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
package bvanseg.kotlincommons.time.api.transformer

import bvanseg.kotlincommons.time.api.KhronoUnit

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
object MinuteTransformer : KhronoTransformer {
    override fun transform(value: Double, unit: KhronoUnit): Double {
        return when (unit) {
            KhronoUnit.NEVER -> KhronoUnit.NEVER_CONSTANT
            KhronoUnit.NANOSECOND -> value * 1_000_000.0 * 1000 * 60
            KhronoUnit.MICROSECOND -> value * 1_000.0 * 1000 * 60
            KhronoUnit.MILLISECOND -> value * 1000.0 * 60
            KhronoUnit.SECOND -> value * 60.0
            KhronoUnit.MINUTE -> value
            KhronoUnit.HOUR -> value / 60.0
            KhronoUnit.HALF_DAY -> value / (60.0 * 24 * 2)
            KhronoUnit.DAY -> value / (60.0 * 24)
            KhronoUnit.WEEK -> value / (60.0 * 24 * 7)
            KhronoUnit.YEAR -> value / (60.0 * 24 * 365)
            KhronoUnit.DECADE -> value / (60.0 * 24 * 365 * 10)
            KhronoUnit.CENTURY -> value / (60.0 * 24 * 365 * 100)
            KhronoUnit.MILLENNIUM -> value / (60.0 * 24 * 365 * 1000)
            KhronoUnit.FOREVER -> KhronoUnit.FOREVER_CONSTANT
        }
    }
}