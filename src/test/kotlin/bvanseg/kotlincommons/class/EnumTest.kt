package bvanseg.kotlincommons.`class`

import bvanseg.kotlincommons.classes.enumValueOfOrNull
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EnumTest {

    enum class Color {
        RED,
        GREEN,
        BLUE
    }

    @Test
    fun enumValueOrNullTest() {
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
}