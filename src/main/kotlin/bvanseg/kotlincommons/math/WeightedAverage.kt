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
 * @author Jacob Glickman
 * @since 2.7.0
 */
class WeightedAverage(totalValue: Double = 0.0, totalWeight: Int = 0) {

    var totalValue = totalValue
        private set

    var totalWeight = totalWeight
        private set

    /**
     * Adds the given [value] and [weight] to the [WeightedAverage], increasing the [totalValue] and the [totalWeight].
     *
     * @param value The value to add to the [WeightedAverage].
     * @param weight The weight to weigh this specific value at.
     */
    fun add(value: Double, weight: Int) {
        totalValue += (value * weight)
        totalWeight += weight
    }

    /**
     * Calculates the weighted average value.
     *
     * @return The weighted average created from the [totalValue] divided by the [totalWeight], or 0 if there is no weight.
     */
    fun getAverage(): Double = if (totalWeight != 0) (totalValue / totalWeight) else 0.0

    /**
     * Copies the current [WeightedAverage] instance to a new instance.
     *
     * @return A copy of the current [WeightedAverage].
     */
    fun copy(): WeightedAverage = WeightedAverage(totalValue, totalWeight)
}