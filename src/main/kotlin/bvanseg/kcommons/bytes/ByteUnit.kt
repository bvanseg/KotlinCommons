package bvanseg.kcommons.bytes

enum class ByteUnit {
	B,
	KB,
	MB,
	GB,
	TB;

	fun to(value: Long, unit: ByteUnit): Long {
		val diff = ordinal - unit.ordinal
		return when {
			diff == 0 -> value
			diff > 0 -> value.shl(diff * 10)
			else -> value.shr(diff * -10)
		}
	}

	fun toBytes(value: Long): Long = to(value, B)
	fun toKilobytes(value: Long): Long = to(value, KB)
	fun toMegabytes(value: Long): Long = to(value, MB)
	fun toGigabytes(value: Long): Long = to(value, GB)
	fun toTerabytes(value: Long): Long = to(value, TB)
}