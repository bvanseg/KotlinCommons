package bvanseg.kotlincommons.graphics

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ColorTests {

    @Test
    fun testRGBValues() {
        // Given
        val color = Color(0x369BE5)

        // When
        val red = color.getRed()
        val green = color.getGreen()
        val blue = color.getBlue()
        color.setAlpha(0xFA)
        val alpha = color.getAlpha()


        // Then
        assertEquals(0xFA, alpha)
        assertEquals(0x36, red)
        assertEquals(0x9B, green)
        assertEquals(0xE5, blue)
    }

    @Test
    fun testColorAddition() {
        // Given
        val redColor = Color(0xFF0000)
        val greenColor = Color(0x00FF00)

        // When
        val newColor = redColor + greenColor

        // Then
        assertEquals(0xFF, newColor.getRed())
        assertEquals(0xFF, newColor.getGreen())
    }

    @Test
    fun testColorSubtraction() {
        // Given
        val redGreenColor = Color(0xFFFF00)
        val greenColor = Color(0x00FF00)

        // When
        val redColor = redGreenColor - greenColor

        // Then
        assertEquals(0xFF, redColor.getRed())
        assertEquals(0x00, redColor.getGreen())
    }

    @Test
    fun testQuadColors() {
        // Given
        val color = Color(0xFAEBD7)
        val quad = color.getRGBA()

        // When
        // Then
        assertEquals(0xFA, quad.first)
        assertEquals(0xEB, quad.second)
        assertEquals(0xD7, quad.third)
        assertEquals(0xFF, quad.fourth)
    }
}