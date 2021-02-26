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

/**
 * Simple helper class used in adjusting running averages.
 *
 * @author Boston Vanseghi
 * @since 2.2.5
 */
class Average(total: Double = 0.0, count: Int = 0) {

    var total = total
        private set

    var count = count
        private set

    /**
     * Adds the given [value] to the [Average], increasing the [total] and the [count].
     *
     * @param value The value to add to the [Average].
     */
    fun add(value: Double) {
        this.total += value
        count++
    }

    /**
     * Calculates the average value.
     *
     * @return The average created from the [total] divided by the [count], or 0 if there is no count.
     */
    fun getAverage(): Double = if (count != 0) (total / count) else 0.0

    /**
     * Copies the current [Average] instance to a new instance.
     *
     * @return A copy of the current [Average].
     */
    fun copy(): Average = Average(total, count)
}
