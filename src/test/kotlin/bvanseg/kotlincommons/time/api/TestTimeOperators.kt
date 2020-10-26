package bvanseg.kotlincommons.time.api

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestTimeOperators {

    @Test
    fun testKTimePlus() {
        // GIVEN
        val oneMinute = 1.minutes
        val oneSecond = 1.seconds

        // WHEN
        val oneMinuteOneSecond = oneMinute + oneSecond
        val oneMinuteOneSecondAlt = oneSecond + oneMinute

        // THEN
        assertEquals(61.0, oneMinuteOneSecond.value)
        assertEquals(KTimeUnit.SECOND, oneMinuteOneSecond.unit)

        assertEquals(61.0, oneMinuteOneSecondAlt.value)
        assertEquals(KTimeUnit.SECOND, oneMinuteOneSecondAlt.unit)

        assertEquals(61.0, (60.seconds + 1.seconds).value)
        assertEquals(61.0, (1.seconds + 60.seconds).value)
    }


    @Test
    fun testKTimeNumberPlus() {
        // GIVEN
        val time = 60.seconds

        // WHEN
        // THEN
        assertEquals(61.0, (time + 1).value)
    }

    @Test
    fun testKTimeNumberPlusAssign() {
        // GIVEN
        val time = 60.seconds

        // WHEN
        time += 1

        // THEN
        assertEquals(61.0, time.value)
    }

    @Test
    fun testKTimeMinus() {
        // GIVEN
        val oneMinute = 1.minutes
        val oneSecond = 1.seconds

        // WHEN
        val oneMinuteOneSecond = oneMinute - oneSecond

        // THEN
        assertEquals(59.0, oneMinuteOneSecond.value)
        assertEquals(KTimeUnit.SECOND, oneMinuteOneSecond.unit)

        assertEquals(59.0, (60.seconds - 1.seconds).value)
    }

    @Test
    fun testKTimeNumberMinus() {
        // GIVEN
        val time = 60.seconds

        // WHEN
        // THEN
        assertEquals(59.0, (time - 1).value)
    }

    @Test
    fun testKTimeNumberMinusAssign() {
        // GIVEN
        val time = 60.seconds

        // WHEN
        time -= 1

        // THEN
        assertEquals(59.0, time.value)
    }
}