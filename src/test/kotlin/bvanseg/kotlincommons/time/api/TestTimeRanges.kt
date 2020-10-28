package bvanseg.kotlincommons.time.api

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestTimeRanges {

    @Test
    fun testKTimeDec() {
        // GIVEN

        // WHEN
        val rangedTime = 0.seconds..1.minutes

        // THEN
        assertEquals(60.0, rangedTime.value)
    }
}