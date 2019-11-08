package bvanseg.kotlincommons.collections

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * @author Boston Vanseghi
 */
internal class DuelMapTest {

	@Test
	fun duelMap() {
        // Given
        val duelMap = DuelMutableMap<Int, String>()

        // When
        duelMap.putLeft("red", 255)
        duelMap.putRight(252, "red")

        // Then
        assertEquals("red", duelMap.getRight(252))
	}
}