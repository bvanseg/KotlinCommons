package bvanseg.kotlincommons.time.api

import bvanseg.kotlincommons.util.project.Experimental
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@Experimental
class TestTimeDecInc {

    @Test
    fun testKTimeDec() {
        // GIVEN
        var minutes = 3.minutes.toMutable()
        var seconds = 3.seconds.toMutable()

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