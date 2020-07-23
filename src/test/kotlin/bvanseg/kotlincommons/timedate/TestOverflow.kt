package bvanseg.kotlincommons.timedate

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestOverflow {

	@Test
	fun testNanoOverflow() {
		// Given
		var timeContainer = UnitBasedTimeContainer(TimeContextUnit.Nano(123_456_789))

		// When
		timeContainer = timeContainer.checkAndCorrectOver()

		// Then
		assertEquals(0, timeContainer.timeObject.year)
		assertEquals(0, timeContainer.timeObject.month)
		assertEquals(0, timeContainer.timeObject.day)
		assertEquals(0, timeContainer.timeObject.hour)
		assertEquals(0, timeContainer.timeObject.minute)
		assertEquals(0, timeContainer.timeObject.second)
		assertEquals(123, timeContainer.timeObject.millis)
		assertEquals(456_789, timeContainer.timeObject.nano)
	}

	@Test
	fun testMultiOverflow() {
		// Given
		var timeContainer = UnitBasedTimeContainer(TimeContextUnit.Second(3700)) // 1hr, 1min, 40 seconds?

		// When
		timeContainer = timeContainer.checkAndCorrectOver()

		// Then
		assertEquals(0, timeContainer.timeObject.year)
		assertEquals(0, timeContainer.timeObject.month)
		assertEquals(0, timeContainer.timeObject.day)
		assertEquals(1, timeContainer.timeObject.hour)
		assertEquals(1, timeContainer.timeObject.minute)
		assertEquals(40, timeContainer.timeObject.second)
		assertEquals(0, timeContainer.timeObject.millis)
		assertEquals(0, timeContainer.timeObject.nano)
	}

	@Test
	fun testAutoOverflowCheck() {
		// Given
		val timeContainer = UnitBasedTimeContainer(TimeContextUnit.Second(3700))

		// When
		// Then
		assertEquals(0, timeContainer.timeObject.year)
		assertEquals(0, timeContainer.timeObject.month)
		assertEquals(0, timeContainer.timeObject.day)
		assertEquals(1, timeContainer.timeObject.hour)
		assertEquals(1, timeContainer.timeObject.minute)
		assertEquals(40, timeContainer.timeObject.second)
		assertEquals(0, timeContainer.timeObject.millis)
		assertEquals(0, timeContainer.timeObject.nano)
	}
}