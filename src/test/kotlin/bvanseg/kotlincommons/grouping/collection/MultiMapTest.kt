package bvanseg.kotlincommons.grouping.collection

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MultiMapTest {

    @Test
    fun testMultiMapSizing() {
        // Given
        val multiMap = MutableMultiValueMap<String, String>()

        // When
        multiMap.put("John", "Smith")
        multiMap.put("Jane", "Doe")
        multiMap.put("John", "Doe")
        multiMap.put("Jane", "Jackson")
        multiMap.put("Jeffrey", "Hill")

        // Then
        assertEquals(1, multiMap["Jeffrey"]?.size)
        assertEquals(2, multiMap["John"]?.size)
        assertEquals(2, multiMap["Jane"]?.size)
    }

    @Test
    fun testMultiMapElements() {
        // Given
        val multiMap = MutableMultiValueMap<String, String>()

        // When
        multiMap.put("John", "Smith")
        multiMap.put("Jane", "Doe")
        multiMap.put("John", "Doe")
        multiMap.put("Jane", "Jackson")
        multiMap.put("Jeffrey", "Hill")

        // Then
        assertTrue(multiMap["Jeffrey"]?.contains("Hill") ?: false)
        assertTrue(multiMap["John"]?.contains("Smith") ?: false)
        assertTrue(multiMap["John"]?.contains("Doe") ?: false)
        assertTrue(multiMap["Jane"]?.contains("Jackson") ?: false)
        assertTrue(multiMap["Jane"]?.contains("Doe") ?: false)
    }
}