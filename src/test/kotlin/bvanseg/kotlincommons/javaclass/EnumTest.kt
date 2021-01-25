package bvanseg.kotlincommons.javaclass

import bvanseg.kotlincommons.grouping.enum.enumValueOfOrNull
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EnumTest {

    enum class Color {
        RED,
        GREEN,
        BLUE
    }

    @Test
    fun enumValueOfOrNullTest() {
        // GIVEN
        val redFromStringLower = enumValueOfOrNull<Color>("red")
        val greenFromStringLower = enumValueOfOrNull<Color>("green")
        val blueFromStringLower = enumValueOfOrNull<Color>("blue")
        val redFromStringUpper = enumValueOfOrNull<Color>("RED")
        val greenFromStringUpper = enumValueOfOrNull<Color>("GREEN")
        val blueFromStringUpper = enumValueOfOrNull<Color>("BLUE")
        val nullEnum = enumValueOfOrNull<Color>(null)
        val nonexistent = enumValueOfOrNull<Color>("YELLOW")

        // WHEN

        // THEN
        assertEquals(null, redFromStringLower)
        assertEquals(null, greenFromStringLower)
        assertEquals(null, blueFromStringLower)

        assertEquals(Color.RED, redFromStringUpper)
        assertEquals(Color.GREEN, greenFromStringUpper)
        assertEquals(Color.BLUE, blueFromStringUpper)

        assertEquals(null, nullEnum)
        assertEquals(null, nonexistent)
    }

    @Test
    fun enumValueOfOrNullCaseTest() {
        // GIVEN
        val redFromStringLower = enumValueOfOrNull<Color>("red", true)
        val greenFromStringLower = enumValueOfOrNull<Color>("green", true)
        val blueFromStringLower = enumValueOfOrNull<Color>("blue", true)
        val redFromStringUpper = enumValueOfOrNull<Color>("RED")
        val greenFromStringUpper = enumValueOfOrNull<Color>("GREEN")
        val blueFromStringUpper = enumValueOfOrNull<Color>("BLUE")
        val nullEnum = enumValueOfOrNull<Color>(null)
        val nonexistent = enumValueOfOrNull<Color>("YELLOW")

        // WHEN

        // THEN
        assertEquals(Color.RED, redFromStringLower)
        assertEquals(Color.GREEN, greenFromStringLower)
        assertEquals(Color.BLUE, blueFromStringLower)

        assertEquals(Color.RED, redFromStringUpper)
        assertEquals(Color.GREEN, greenFromStringUpper)
        assertEquals(Color.BLUE, blueFromStringUpper)

        assertEquals(null, nullEnum)
        assertEquals(null, nonexistent)
    }
}