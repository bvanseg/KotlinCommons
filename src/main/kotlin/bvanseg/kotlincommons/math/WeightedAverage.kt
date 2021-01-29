package bvanseg.kotlincommons.math

/**
 * @author Jacob Glickman
 * @since 2.7.0
 */
class WeightedAverage(var totalValue: Double = 0.0, private var totalWeight: Int = 0) {

    /**
     * Adds the given [value] and [weight] to the [WeightedAverage], increasing the [totalValue] and the [totalWeight].
     *
     * @param value The value to add to the [WeightedAverage].
     * @param weight The weight to weigh this specific value at.
     */
    fun add(value: Double, weight: Int) {
        this.totalValue += (value * weight)
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
    fun copy(): WeightedAverage = WeightedAverage(this.totalValue, this.totalWeight)
}