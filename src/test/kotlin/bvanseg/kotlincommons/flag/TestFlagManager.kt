package bvanseg.kotlincommons.flag

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

class TestFlagManager {

    enum class TestEnum {
        RED,
        GREEN,
        BLUE
    }

    private val flagManager = FlagManager.createManager<TestEnum>()

    @Test
    fun testWriting() {
        // GIVEN
        val flags = flagManager.write(EnumSet.of(TestEnum.RED, TestEnum.GREEN))

        // WHEN

        // THEN
        assertEquals(3L, flags)
    }

    @Test
    fun testExtensiveWriting() {
        // GIVEN
        val flags = flagManager.extensiveWrite(EnumSet.of(TestEnum.RED, TestEnum.GREEN))

        // WHEN

        // THEN
        assertEquals(3, flags.toLong())
    }


    @Test
    fun testReading() {
        // GIVEN
        val none = 0L
        val r = 1L
        val g = 2L
        val b = 4L
        val rg = 3L
        val rb = 5L
        val gb = 6L
        val rgb = 7L

        // WHEN

        // THEN
        assertEquals(EnumSet.noneOf(TestEnum::class.java), flagManager.read(none))
        assertEquals(EnumSet.of(TestEnum.RED), flagManager.read(r))
        assertEquals(EnumSet.of(TestEnum.GREEN), flagManager.read(g))
        assertEquals(EnumSet.of(TestEnum.BLUE), flagManager.read(b))
        assertEquals(EnumSet.of(TestEnum.RED, TestEnum.GREEN), flagManager.read(rg))
        assertEquals(EnumSet.of(TestEnum.RED, TestEnum.BLUE), flagManager.read(rb))
        assertEquals(EnumSet.of(TestEnum.GREEN, TestEnum.BLUE), flagManager.read(gb))
        assertEquals(EnumSet.of(TestEnum.RED, TestEnum.GREEN, TestEnum.BLUE), flagManager.read(rgb))
    }
}