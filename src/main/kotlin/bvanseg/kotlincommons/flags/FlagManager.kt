package bvanseg.kotlincommons.flags

import bvanseg.kotlincommons.collections.DualHashMap
import java.util.*

/**
*
* @author Boston Vanseghi
* @since 2.2.4
*/
class FlagManager<T : Enum<T>>(private val enumClass: Class<T>) {

	private val map = DualHashMap<Long, T>()

	/**
	* Current offset in bits. Max is 64.
	*/
	private var currentOffset: Long = 0

	companion object {
		inline fun <reified T : Enum<T>> createManager(): FlagManager<T> {
			val manager = FlagManager(T::class.java)

			val values = enumValues<T>()

			for (value in values)
				manager.register(value)

			return manager
		}
	}

	fun register(value: T) {
		map[currentOffset++] = value
	}

	fun getOffsetFor(value: Enum<T>): Long = map.reverse()[value]!!

	fun getValue(offset: Long): Enum<T>? {
		if (offset < 0)
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
	fun read(number: Long): EnumSet<T> {
		val values = EnumSet.noneOf(enumClass)

		for (offset in 0L..31L) {
			if (number and (1L shl offset.toInt()) != 0L)
				if (map[offset] != null)
					values.add(map[offset]!!)
		}

		return values
	}

	fun write(values: EnumSet<T>): Long {
		var currentValue = 0L

		for (value in values) {
			val offset = getOffsetFor(value)
			currentValue = currentValue or (1L shl offset.toInt())
		}

		return currentValue
	}
}
