package bvanseg.kotlincommons.mathematics

/**
* Simple helper class used in adjusting running averages.
*
* @author Boston Vanseghi
* @since 2.2.5
*/
class Average(var total: Double = 0.0, private var count: Int = 0) {

	fun add(value: Double) {
		this.total += value
		count++
	}

	fun getAverage(): Double = if (count != 0) (total / count) else 0.0

	fun copy(): Average = Average(this.total, this.count)
}
