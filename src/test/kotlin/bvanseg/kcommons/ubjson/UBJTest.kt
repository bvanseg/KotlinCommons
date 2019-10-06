package bvanseg.kcommons.ubjson

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

/**
 * @author bright_spark
 */
internal class UBJTest {
	private lateinit var ubj: UBJ

	@BeforeEach
	fun setup() {
		ubj = UBJ()
	}

	@Test
	fun `null`() {
		// Given
		// When
		ubj.setNull("null")

		// Then
		val result = ubj.isNull("null")
		assertTrue(result)
	}

	@Test
	fun boolean() {
		// Given
		val boolean = true

		// When
		ubj.setBoolean("boolean", boolean)

		// Then
		assertEquals(true, ubj.getBoolean("boolean"))
	}

	@Test
	fun byte() {
		// Given
		val byte = Byte.MAX_VALUE

		// When
		ubj.setByte("byte", byte)

		// Then
		assertEquals(Byte.MAX_VALUE, ubj.getByte("byte"))
	}

	@Test
	fun short() {
		// Given
		val short = Short.MAX_VALUE

		// When
		ubj.setShort("short", short)

		// Then
		assertEquals(Short.MAX_VALUE, ubj.getShort("short"))
	}

	@Test
	fun int() {
		// Given
		val int = Int.MAX_VALUE

		// When
		ubj.setInt("int", int)

		// Then
		assertEquals(Int.MAX_VALUE, ubj.getInt("int"))
	}

	@Test
	fun long() {
		// Given
		val long = Long.MAX_VALUE

		// When
		ubj.setLong("long", long)

		// Then
		assertEquals(Long.MAX_VALUE, ubj.getLong("long"))
	}

	@Test
	fun float() {
		// Given
		val float = Float.MAX_VALUE

		// When
		ubj.setFloat("float", float)

		// Then
		assertEquals(Float.MAX_VALUE, ubj.getFloat("float"))
	}

	@Test
	fun double() {
		// Given
		val double = Double.MAX_VALUE

		// When
		ubj.setDouble("double", double)

		// Then
		assertEquals(Double.MAX_VALUE, ubj.getDouble("double"))
	}

	@Test
	fun string() {
		// Given
		val string = "test"

		// When
		ubj.setString("string", string)

		// Then
		assertEquals("test", ubj.getString("string"))
	}

	@Test
	fun booleanArray() {
		// Given
		val array = booleanArrayOf(false, true)

		// When
		ubj.setBooleanArray("array", *array)

		// Then
		val result = ubj.getBooleanArray("array")
		assertNotNull(result)
		assertEquals(false, result!![0])
		assertEquals(true, result[1])
	}

	@Test
	fun byteArray() {
		// Given
		val array = byteArrayOf(0, 1)

		// When
		ubj.setByteArray("array", *array)

		// Then
		val result = ubj.getByteArray("array")
		assertNotNull(result)
		assertEquals(0.toByte(), result!![0])
		assertEquals(1.toByte(), result[1])
	}

	@Test
	fun shortArray() {
		// Given
		val array = shortArrayOf(0, 1)

		// When
		ubj.setShortArray("array", *array)

		// Then
		val result = ubj.getShortArray("array")
		assertNotNull(result)
		assertEquals(0.toShort(), result!![0])
		assertEquals(1.toShort(), result[1])
	}

	@Test
	fun intArray() {
		// Given
		val array = intArrayOf(0, 1)

		// When
		ubj.setIntArray("array", *array)

		// Then
		val result = ubj.getIntArray("array")
		assertNotNull(result)
		assertEquals(0, result!![0])
		assertEquals(1, result[1])
	}

	@Test
	fun longArray() {
		// Given
		val array = longArrayOf(0, 1)

		// When
		ubj.setLongArray("array", *array)

		// Then
		val result = ubj.getLongArray("array")
		assertNotNull(result)
		assertEquals(0.toLong(), result!![0])
		assertEquals(1.toLong(), result[1])
	}

	@Test
	fun floatArray() {
		// Given
		val array = floatArrayOf(0F, 1F)

		// When
		ubj.setFloatArray("array", *array)

		// Then
		val result = ubj.getFloatArray("array")
		assertNotNull(result)
		assertEquals(0F, result!![0])
		assertEquals(1F, result[1])
	}

	@Test
	fun doubleArray() {
		// Given
		val array = doubleArrayOf(0.0, 1.0)

		// When
		ubj.setDoubleArray("array", *array)

		// Then
		val result = ubj.getDoubleArray("array")
		assertNotNull(result)
		assertEquals(0.0, result!![0])
		assertEquals(1.0, result[1])
	}

	@Test
	fun stringArray() {
		// Given
		val array = arrayOf("test1", "test2")

		// When
		ubj.setStringArray("array", *array)

		// Then
		val result = ubj.getStringArray("array")
		assertNotNull(result)
		assertEquals("test1", result!![0])
		assertEquals("test2", result[1])
	}

	@Test
	fun uuid() {
		// Given
		val uuid = UUID(1234, 5678)

		// When
		ubj.setUUID("uuid", uuid)

		// Then
		val result = ubj.getUUID("uuid")
		assertNotNull(result)
		assertEquals(1234.toLong(), result!!.mostSignificantBits)
		assertEquals(5678.toLong(), result.leastSignificantBits)
	}
}