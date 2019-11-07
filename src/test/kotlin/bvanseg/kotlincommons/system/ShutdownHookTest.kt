package bvanseg.kotlincommons.system

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * @author Boston Vanseghi
 */
internal class ShutdownHookTest {

	@Test
	fun testShutdownHooks() {
		// Given
		var flag = false

		// When
		runOnExit {
			println("Hello, world!")
			flag = true
			assertTrue(flag)
		}

		// Then
		assertFalse(flag)
	}
}