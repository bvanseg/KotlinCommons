package bvanseg.kotlincommons.time.api

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestTimeDecInc {

    @Test
    fun testKTimeDec() {
        // GIVEN
        var minutes = 3.minutes
        var seconds = 3.seconds

        // WHEN

        // THEN
        assertEquals(2.0, (--minutes).value)
        assertEquals(1.0, (minutes--).value)

        assertEquals(2.0, (++minutes).value)
        assertEquals(3.0, (minutes++).value)

        assertEquals(2.0, (--seconds).value)
        assertEquals(1.0, (seconds--).value)

        assertEquals(2.0, (++seconds).value)
        assertEquals(3.0, (seconds++).value)
    }
}