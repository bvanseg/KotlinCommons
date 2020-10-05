package bvanseg.kotlincommons.collection

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DualHashMapTest {

    @Test
    fun testdualMap() {
        // Given
        val dualMap = DualHashMap<String, Int>()

        // When
        dualMap["Bob"] = 42

        // Then
        assertEquals("Bob", dualMap.reverse()[42])
    }

    @Test
    fun testReverseEdit() {
        // Given
        val dualMap = DualHashMap<String, Int>()

        // When
        dualMap.reverse()[42] = "Bob"

        // Then
        assertEquals(42, dualMap["Bob"])
    }
}