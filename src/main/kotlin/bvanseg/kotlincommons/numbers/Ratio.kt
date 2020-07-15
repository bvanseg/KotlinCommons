package bvanseg.kotlincommons.numbers

/**
 * @author Boston Vanseghi
 * @since 2.2.5
 */
data class Ratio<T : Number>(var left: T, var right: T) {

	fun getLeftPercent(): Float =
		((left.toDouble() / (left.toDouble() + right.toDouble())) * 100).toFloat()

	fun getRightPercent(): Float =
		((right.toDouble() / (left.toDouble() + right.toDouble())) * 100).toFloat()
}