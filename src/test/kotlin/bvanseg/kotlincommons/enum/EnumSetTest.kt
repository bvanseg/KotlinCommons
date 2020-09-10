package bvanseg.kotlincommons.enum

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class EnumSetTest {

    enum class Color {
        RED,
        GREEN,
        BLUE
    }

    @Test
    fun testEmptyEnumSetOf() {
        // GIVEN
        val set = enumSetOf<Color>()

        // WHEN
        // THEN
        assertTrue(set.isEmpty())
    }

    @Test
    fun testSingleEnumSetOf() {
        // GIVEN
        val set = enumSetOf(Color.RED)

        // WHEN
        // THEN
        assertEquals(1, set.size)
        assertTrue(set.first() == Color.RED)
    }

    @Test
    fun testMultiEnumSetOf() {
        // GIVEN
        val set = enumSetOf(Color.RED, Color.GREEN, Color.BLUE)
        val iter = set.iterator()

        // WHEN
        // THEN
        assertEquals(3, set.size)
        assertTrue(iter.next() == Color.RED)
        assertTrue(iter.next() == Color.GREEN)
        assertTrue(iter.next() == Color.BLUE)
    }
}