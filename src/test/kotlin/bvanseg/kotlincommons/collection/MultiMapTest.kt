package bvanseg.kotlincommons.collection

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MultiMapTest {

    @Test
    fun testMultiMapSizing() {
        // Given
        val multiMap = MutableMultiMap<String, String>()

        // When
        multiMap.put("John", "Smith")
        multiMap.put("Jane", "Doe")
        multiMap.put("John", "Doe")
        multiMap.put("Jane", "Jackson")
        multiMap.put("Jeffrey", "Hill")

        // Then
        assertEquals(1, multiMap["Jeffrey"].size)
        assertEquals(2, multiMap["John"].size)
        assertEquals(2, multiMap["Jane"].size)
    }

    @Test
    fun testMultiMapElements() {
        // Given
        val multiMap = MutableMultiMap<String, String>()

        // When
        multiMap.put("John", "Smith")
        multiMap.put("Jane", "Doe")
        multiMap.put("John", "Doe")
        multiMap.put("Jane", "Jackson")
        multiMap.put("Jeffrey", "Hill")

        // Then
        assertTrue(multiMap["Jeffrey"].contains("Hill"))
        assertTrue(multiMap["John"].contains("Smith"))
        assertTrue(multiMap["John"].contains("Doe"))
        assertTrue(multiMap["Jane"].contains("Jackson"))
        assertTrue(multiMap["Jane"].contains("Doe"))
    }
}