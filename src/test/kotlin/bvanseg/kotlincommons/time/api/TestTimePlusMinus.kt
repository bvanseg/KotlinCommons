package bvanseg.kotlincommons.time.api

import bvanseg.kotlincommons.time.api.operator.plus
import bvanseg.kotlincommons.time.api.operator.minus
import bvanseg.kotlincommons.util.project.Experimental
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@Experimental
class TestTimePlusMinus {

    @Test
    fun testKTimePlus() {
        // GIVEN
        val oneMinute: MutableKhrono = 1.minutes.toMutable()
        val oneSecond: MutableKhrono = 1.seconds.toMutable()

        // WHEN
        val oneMinuteOneSecond: MutableKhrono = oneMinute + oneSecond
        val oneMinuteOneSecondAlt: MutableKhrono = oneSecond + oneMinute

        // THEN
        assertEquals(61.0, oneMinuteOneSecond.value)
        assertEquals(KhronoUnit.SECOND, oneMinuteOneSecond.unit)

        assertEquals(61.0, oneMinuteOneSecondAlt.value)
        assertEquals(KhronoUnit.SECOND, oneMinuteOneSecondAlt.unit)

        assertEquals(61.0, (60.seconds + 1.seconds).value)
        assertEquals(61.0, (1.seconds + 60.seconds).value)
    }


    @Test
    fun testKTimeNumberPlus() {
        // GIVEN
        val time: MutableKhrono = 60.seconds.toMutable()

        // WHEN
        // THEN
        assertEquals(61.0, (time + 1).value)
    }

    @Test
    fun testKTimeNumberPlusAssign() {
        // GIVEN
        var time: MutableKhrono = 60.seconds.toMutable()

        // WHEN
        time += 1

        // THEN
        assertEquals(61.0, time.value)
    }

    @Test
    fun testKTimeMinus() {
        // GIVEN
        val oneMinute: MutableKhrono = 1.minutes.toMutable()
        val oneSecond: MutableKhrono = 1.seconds.toMutable()

        // WHEN
        val oneMinuteOneSecond: MutableKhrono = oneMinute - oneSecond

        // THEN
        assertEquals(59.0, oneMinuteOneSecond.value)
        assertEquals(KhronoUnit.SECOND, oneMinuteOneSecond.unit)

        assertEquals(59.0, (60.seconds - 1.seconds).value)
    }

    @Test
    fun testKTimeNumberMinus() {
        // GIVEN
        val time: MutableKhrono = 60.seconds.toMutable()

        // WHEN
        // THEN
        assertEquals(59.0, (time - 1).value)
    }

    @Test
    fun testKTimeNumberMinusAssign() {
        // GIVEN
        var time: MutableKhrono = 60.seconds.toMutable()

        // WHEN
        time -= 1

        // THEN
        assertEquals(59.0, time.value)
    }
}