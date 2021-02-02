package bvanseg.kotlincommons.time.api

import bvanseg.kotlincommons.time.api.operator.compareTo
import bvanseg.kotlincommons.util.project.Experimental
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@Experimental
class TestTimeComparisons {

    @Test
    fun testKTimeComparisons() {
        // GIVEN
        val oneMinute = 1.minutes
        val oneSecond = 1.seconds
        val sixtySeconds = 60.seconds

        // WHEN

        // THEN
        assertEquals(true, oneMinute > oneSecond)
        assertEquals(false, oneSecond > oneMinute)
        assertEquals(false, oneSecond == oneMinute)

        assertEquals(false, oneMinute > sixtySeconds)
        assertEquals(false, sixtySeconds > oneMinute)
        assertEquals(true, sixtySeconds == oneMinute)
    }

    @Test
    fun testKTimeNumberComparisons() {
        // GIVEN
        val oneSecond = 1.seconds

        // WHEN

        // THEN
        assertEquals(true, 60 > oneSecond)
        assertEquals(false, 60 < oneSecond)
    }
}