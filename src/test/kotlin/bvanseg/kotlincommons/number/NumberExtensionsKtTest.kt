package bvanseg.kotlincommons.number

import argStreamOf
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

/**
 * @author bright_spark
 */
internal class NumberExtensionsKtTest {
	companion object {
		@JvmStatic
		fun evenNumbers() = argStreamOf(2.toByte(), 4.toShort(), 8, 16L, 32F, 64.0)

		@JvmStatic
		fun oddNumbers() = argStreamOf(1.toByte(), 3.toShort(), 7, 15L, 31F, 63.0)
	}

	@ParameterizedTest
	@MethodSource("evenNumbers")
	fun isEven_true(number: Number) {
		// Given
		// When
		val result = number.isEven()

		// Then
		assertTrue(result)
	}

	@ParameterizedTest
	@MethodSource("oddNumbers")
	fun isEven_false(number: Number) {
		// Given
		// When
		val result = number.isEven()

		// Then
		assertFalse(result)
	}

	@ParameterizedTest
	@MethodSource("oddNumbers")
	fun isOdd_true(number: Number) {
		// Given
		// When
		val result = number.isOdd()

		// Then
		assertTrue(result)
	}

	@ParameterizedTest
	@MethodSource("evenNumbers")
	fun isOdd_false(number: Number) {
		// Given
		// When
		val result = number.isOdd()

		// Then
		assertFalse(result)
	}

	@Test
	fun isPositive_true() {
		// Given
		// When
		var positive = false
		1.ifPositive { positive = true }

		// Then
		assertTrue(positive)
	}

	@Test
	fun isPositive_false() {
		// Given
		// When
		var positive = false
		(-1).ifPositive { positive = true }

		// Then
		assertFalse(positive)
	}

	@Test
	fun isNegative_true() {
		// Given
		// When
		var negative = false
		(-1).ifNegative { negative = true }

		// Then
		assertTrue(negative)
	}

	@Test
	fun isNegative_false() {
		// Given
		// When
		var negative = false
		1.ifNegative { negative = true }

		// Then
		assertFalse(negative)
	}
}
