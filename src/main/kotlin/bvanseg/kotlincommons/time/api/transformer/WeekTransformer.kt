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

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
import bvanseg.kotlincommons.time.api.KhronoUnit

object WeekTransformer : KhronoTransformer {
    override fun transform(value: Double, unit: KhronoUnit): Double {
        return when (unit) {
            KhronoUnit.NEVER -> KhronoUnit.NEVER_CONSTANT
            KhronoUnit.NANOSECOND -> value * 1_000_000.0 * 1000 * 60 * 60 * 24 * 7
            KhronoUnit.MICROSECOND -> value * 1_000.0 * 1000 * 60 * 60 * 24 * 7
            KhronoUnit.MILLISECOND -> value * 1000.0 * 60 * 60 * 24 * 7
            KhronoUnit.SECOND -> value * 60.0 * 60 * 24 * 7
            KhronoUnit.MINUTE -> value * 60.0 * 24 * 7
            KhronoUnit.HOUR -> value * 24 * 7
            KhronoUnit.HALF_DAY -> value * 7 * 2
            KhronoUnit.DAY -> value * 7
            KhronoUnit.WEEK -> value
            KhronoUnit.YEAR -> (value * 7) / 365.0
            KhronoUnit.DECADE -> (value * 7) / (365.0 * 10)
            KhronoUnit.CENTURY -> (value * 7) / (365.0 * 100)
            KhronoUnit.MILLENNIUM -> (value * 7) / (365.0 * 1000)
            KhronoUnit.FOREVER -> KhronoUnit.FOREVER_CONSTANT
        }
    }
}