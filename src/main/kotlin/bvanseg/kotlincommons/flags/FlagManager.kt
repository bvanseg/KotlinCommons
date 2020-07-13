package bvanseg.kotlincommons.flags

import bvanseg.kotlincommons.collections.DualHashMap

/**
*
* @author Boston Vanseghi
* @since 2.2.4
*/
class FlagManager<T : Enum<T>> {

	private val map = DualHashMap<Int, Enum<T>>()

	/**
	* Current offset in bits. Max is 64.
	*/
	private var currentOffset: Int = 0

	companion object {
		const val MAX_OFFSET = Long.SIZE_BITS - 1

		inline fun <reified T : Enum<T>> register(): FlagManager<T> {
			val manager = FlagManager<T>()

			val values = enumValues<T>()

			for (value in values)
				manager.register(value)

			return manager
		}
	}

	fun register(value: Enum<T>) {
		// Overflow!
		if (currentOffset + 1 > MAX_OFFSET)
			throw Exception("Maximum flag manager size exceeded: ${currentOffset + 1} given value $value")

		println("registering $value at offset $currentOffset")
		map[currentOffset++] = value
	}

	fun getOffsetFor(value: Enum<T>): Int = map.reverse().get(value)!!

	fun getValue(offset: Int): Enum<T>? {
		if (offset < 0 || offset > MAX_OFFSET)
			throw Exception("Invalid offset of $offset, must fall in range of 0 - 31.")

		return map[offset] // Can't assert here, offset might be something absurd.
	}

	/**
	* Reads in a number and spits out a list of values.
	*
	* @param number - The number to read. Must not be negative.
	*
	* @return a [List] of [Enum]s of type [T].
	*/
	fun read(number: Long): List<Enum<T>> {
		val values = mutableListOf<Enum<T>>()

		println("reading value: ${number.toString(2)}")

		for (offset in 0..31) {
			if (number and (1L shl offset) != 0L)
				if (map[offset] != null)
					values.add(map[offset]!!)
		}

		return values
	}

	fun build(values: List<Enum<T>>): Long {
		var currentValue = 0L

		for (value in values) {
			val offset = getOffsetFor(value)
			currentValue = currentValue or (1L shl offset)
		}

		return currentValue
	}
}
