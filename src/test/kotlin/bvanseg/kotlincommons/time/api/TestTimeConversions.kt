package bvanseg.kotlincommons.time.api

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestTimeConversions {

    @Test
    fun testNanoseconds() {
        assertEquals(1_000.0.nanoseconds, 1.microseconds into nanoseconds)
        assertEquals(1_000_000.0.nanoseconds, 1.milliseconds into nanoseconds)
        assertEquals((1_000_000.0 * 1000).nanoseconds, 1.seconds into nanoseconds)
        assertEquals((1_000_000.0 * 1000 * 60).nanoseconds, 1.minutes into nanoseconds)
        assertEquals((1_000_000.0 * 1000 * 60 * 60).nanoseconds, 1.hours into nanoseconds)
        assertEquals((1_000_000.0 * 1000 * 60 * 60 * 24).nanoseconds, 1.days into nanoseconds)
        assertEquals((1_000_000.0 * 1000 * 60 * 60 * 24 * 7).nanoseconds, 1.weeks into nanoseconds)
        assertEquals((1_000_000.0 * 1000 * 60 * 60 * 24 * 365).nanoseconds, 1.years into nanoseconds)
        assertEquals((1_000_000.0 * 1000 * 60 * 60 * 24 * 365 * 10).nanoseconds, 1.decades into nanoseconds)
        assertEquals((1_000_000.0 * 1000 * 60 * 60 * 24 * 365 * 100).nanoseconds, 1.centuries into nanoseconds)
        assertEquals((1_000_000.0 * 1000 * 60 * 60 * 24 * 365 * 1000).nanoseconds, 1.millenniums into nanoseconds)
    }

    @Test
    fun testMicroseconds() {
        assertEquals((1 / 1_000.0).microseconds, 1.nanoseconds into microseconds)
        assertEquals(1.microseconds, 1.microseconds into microseconds)
        assertEquals(1000.microseconds, 1.milliseconds into microseconds)
        assertEquals((1000.0 * 1000).microseconds, 1.seconds into microseconds)
        assertEquals((1000.0 * 1000 * 60).microseconds, 1.minutes into microseconds)
        assertEquals((1000.0 * 1000 * 60 * 60).microseconds, 1.hours into microseconds)
        assertEquals((1000.0 * 1000 * 60 * 60 * 24).microseconds, 1.days into microseconds)
        assertEquals((1000.0 * 1000 * 60 * 60 * 24 * 7).microseconds, 1.weeks into microseconds)
        assertEquals((1000.0 * 1000 * 60 * 60 * 24 * 365).microseconds, 1.years into microseconds)
        assertEquals((1000.0 * 1000 * 60 * 60 * 24 * 365 * 10).microseconds, 1.decades into microseconds)
        assertEquals((1000.0 * 1000 * 60 * 60 * 24 * 365 * 100).microseconds, 1.centuries into microseconds)
        assertEquals((1000.0 * 1000 * 60 * 60 * 24 * 365 * 1000).microseconds, 1.millenniums into microseconds)
    }

    @Test
    fun testMilliseconds() {
        assertEquals((1 / 1_000_000.0).milliseconds, 1.nanoseconds into milliseconds)
        assertEquals((1 / 1_000.0).milliseconds, 1.microseconds into milliseconds)
        assertEquals(1.milliseconds, 1.milliseconds into milliseconds)
        assertEquals(1000.milliseconds, 1.seconds into milliseconds)
        assertEquals((1000.0 * 60).milliseconds, 1.minutes into milliseconds)
        assertEquals((1000.0 * 60 * 60).milliseconds, 1.hours into milliseconds)
        assertEquals((1000.0 * 60 * 60 * 24).milliseconds, 1.days into milliseconds)
        assertEquals((1000.0 * 60 * 60 * 24 * 7).milliseconds, 1.weeks into milliseconds)
        assertEquals((1000.0 * 60 * 60 * 24 * 365).milliseconds, 1.years into milliseconds)
        assertEquals((1000.0 * 60 * 60 * 24 * 365 * 10).milliseconds, 1.decades into milliseconds)
        assertEquals((1000.0 * 60 * 60 * 24 * 365 * 100).milliseconds, 1.centuries into milliseconds)
        assertEquals((1000.0 * 60 * 60 * 24 * 365 * 1000).milliseconds, 1.millenniums into milliseconds)
    }

    @Test
    fun testSeconds() {
        assertEquals((1 / (1_000_000.0 * 1_000)).seconds, 1.nanoseconds into seconds)
        assertEquals((1 / 1_000_000.0).seconds, 1.microseconds into seconds)
        assertEquals((1 / 1000.0).seconds, 1.milliseconds into seconds)
        assertEquals(1.seconds, 1.seconds into seconds)
        assertEquals(60.seconds, 1.minutes into seconds)
        assertEquals((60.0 * 60).seconds, 1.hours into seconds)
        assertEquals((60.0 * 60 * 24).seconds, 1.days into seconds)
        assertEquals((60.0 * 60 * 24 * 7).seconds, 1.weeks into seconds)
        assertEquals((60.0 * 60 * 24 * 365).seconds, 1.years into seconds)
        assertEquals((60.0 * 60 * 24 * 365 * 10).seconds, 1.decades into seconds)
        assertEquals((60.0 * 60 * 24 * 365 * 100).seconds, 1.centuries into seconds)
        assertEquals((60.0 * 60 * 24 * 365 * 1000).seconds, 1.millenniums into seconds)
    }

    @Test
    fun testMinutes() {
        assertEquals((1 / (1_000_000.0 * 1_000 * 60)).minutes, 1.nanoseconds into minutes)
        assertEquals((1 / (1_000_000.0 * 60)).minutes, 1.microseconds into minutes)
        assertEquals((1 / (1000.0 * 60)).minutes, 1.milliseconds into minutes)
        assertEquals((1 / 60.0).minutes, 1.seconds into minutes)
        assertEquals(1.minutes, 1.minutes into minutes)
        assertEquals(60.minutes, 1.hours into minutes)
        assertEquals((60 * 24).minutes, 1.days into minutes)
        assertEquals((60 * 24 * 7).minutes, 1.weeks into minutes)
        assertEquals((60 * 24 * 365).minutes, 1.years into minutes)
        assertEquals((60 * 24 * 365 * 10).minutes, 1.decades into minutes)
        assertEquals((60 * 24 * 365 * 100).minutes, 1.centuries into minutes)
        assertEquals((60 * 24 * 365 * 1000).minutes, 1.millenniums into minutes)
    }

    @Test
    fun testHours() {
        assertEquals((1 / (1_000_000.0 * 1_000 * 60 * 60)).hours, 1.nanoseconds into hours)
        assertEquals((1 / (1_000_000.0 * 60 * 60)).hours, 1.microseconds into hours)
        assertEquals((1 / (1000.0 * 60 * 60)).hours, 1.milliseconds into hours)
        assertEquals((1 / (60.0 * 60)).hours, 1.seconds into hours)
        assertEquals((1 / 60.0).hours, 1.minutes into hours)
        assertEquals(1.hours, 1.hours into hours)
        assertEquals(24.hours, 1.days into hours)
        assertEquals((24 * 7).hours, 1.weeks into hours)
        assertEquals((24 * 365).hours, 1.years into hours)
        assertEquals((24 * 365 * 10).hours, 1.decades into hours)
        assertEquals((24 * 365 * 100).hours, 1.centuries into hours)
        assertEquals((24 * 365 * 1000).hours, 1.millenniums into hours)
    }

    @Test
    fun testDays() {
        assertEquals((1 / (1_000_000.0 * 1_000 * 60 * 60 * 24)).days, 1.nanoseconds into days)
        assertEquals((1 / (1_000_000.0 * 60 * 60 * 24)).days, 1.microseconds into days)
        assertEquals((1 / (1000.0 * 60 * 60 * 24)).days, 1.milliseconds into days)
        assertEquals((1 / (60.0 * 60 * 24)).days, 1.seconds into days)
        assertEquals((1 / (60.0 * 24)).days, 1.minutes into days)
        assertEquals((1 / 24.0).days, 1.hours into days)
        assertEquals(1.days, 1.days into days)
        assertEquals(7.days, 1.weeks into days)
        assertEquals((365).days, 1.years into days)
        assertEquals((365 * 10).days, 1.decades into days)
        assertEquals((365 * 100).days, 1.centuries into days)
        assertEquals((365 * 1000).days, 1.millenniums into days)
    }

    @Test
    fun testWeeks() {
        assertEquals((1 / (1_000_000.0 * 1_000 * 60 * 60 * 24 * 7)).weeks, 1.nanoseconds into weeks)
        assertEquals((1 / (1_000_000.0 * 60 * 60 * 24 * 7)).weeks, 1.microseconds into weeks)
        assertEquals((1 / (1000.0 * 60 * 60 * 24 * 7)).weeks, 1.milliseconds into weeks)
        assertEquals((1 / (60.0 * 60 * 24 * 7)).weeks, 1.seconds into weeks)
        assertEquals((1 / (60.0 * 24 * 7)).weeks, 1.minutes into weeks)
        assertEquals((1 / (24.0 * 7)).weeks, 1.hours into weeks)
        assertEquals((1 / 7.0).weeks, 1.days into weeks)
        assertEquals(1.weeks, 1.weeks into weeks)
        assertEquals((365 / 7.0).weeks, 1.years into weeks)
        assertEquals(((365 / 7.0) * 10).weeks, 1.decades into weeks)
        assertEquals(((365 / 7.0) * 100).weeks, 1.centuries into weeks)
        assertEquals(((365 / 7.0) * 1000).weeks, 1.millenniums into weeks)
    }

    @Test
    fun testYears() {
        assertEquals((1 / (1_000_000.0 * 1_000 * 60 * 60 * 24 * 365)).years, 1.nanoseconds into years)
        assertEquals((1 / (1_000_000.0 * 60 * 60 * 24 * 365)).years, 1.microseconds into years)
        assertEquals((1 / (1000.0 * 60 * 60 * 24 * 365)).years, 1.milliseconds into years)
        assertEquals((1 / (60.0 * 60 * 24 * 365)).years, 1.seconds into years)
        assertEquals((1 / (60.0 * 24 * 365)).years, 1.minutes into years)
        assertEquals((1 / (24.0 * 365)).years, 1.hours into years)
        assertEquals((1 / 365.0).years, 1.days into years)
        assertEquals((7 / (365.0)).years, 1.weeks into years)
        assertEquals(1.years, 1.years into years)
        assertEquals(10.years, 1.decades into years)
        assertEquals(100.years, 1.centuries into years)
        assertEquals(1000.years, 1.millenniums into years)
    }

    @Test
    fun testDecades() {
        assertEquals((1 / (1_000_000.0 * 1_000 * 60 * 60 * 24 * 365 * 10)).decades, 1.nanoseconds into decades)
        assertEquals((1 / (1_000_000.0 * 60 * 60 * 24 * 365 * 10)).decades, 1.microseconds into decades)
        assertEquals((1 / (1000.0 * 60 * 60 * 24 * 365 * 10)).decades, 1.milliseconds into decades)
        assertEquals((1 / (60.0 * 60 * 24 * 365 * 10)).decades, 1.seconds into decades)
        assertEquals((1 / (60.0 * 24 * 365 * 10)).decades, 1.minutes into decades)
        assertEquals((1 / (24.0 * 365 * 10)).decades, 1.hours into decades)
        assertEquals((1 / (365.0 * 10)).decades, 1.days into decades)
        assertEquals((7 / (365.0 * 10)).decades, 1.weeks into decades)
        assertEquals((1 / 10.0).decades, 1.years into decades)
        assertEquals(1.decades, 1.decades into decades)
        assertEquals(10.decades, 1.centuries into decades)
        assertEquals(100.decades, 1.millenniums into decades)
    }

    @Test
    fun testCenturies() {
        assertEquals((1 / (1_000_000.0 * 1_000 * 60 * 60 * 24 * 365 * 100)).centuries, 1.nanoseconds into centuries)
        assertEquals((1 / (1_000_000.0 * 60 * 60 * 24 * 365 * 100)).centuries, 1.microseconds into centuries)
        assertEquals((1 / (1000.0 * 60 * 60 * 24 * 365 * 100)).centuries, 1.milliseconds into centuries)
        assertEquals((1 / (60.0 * 60 * 24 * 365 * 100)).centuries, 1.seconds into centuries)
        assertEquals((1 / (60.0 * 24 * 365 * 100)).centuries, 1.minutes into centuries)
        assertEquals((1 / (24.0 * 365 * 100)).centuries, 1.hours into centuries)
        assertEquals((1 / (365.0 * 100)).centuries, 1.days into centuries)
        assertEquals((7 / (365.0 * 100)).centuries, 1.weeks into centuries)
        assertEquals((1 / 100.0).centuries, 1.years into centuries)
        assertEquals((1 / 10.0).centuries, 1.decades into centuries)
        assertEquals(1.centuries, 1.centuries into centuries)
        assertEquals(10.centuries, 1.millenniums into centuries)
    }

    @Test
    fun testMillenniums() {
        assertEquals(
            (1 / (1_000_000.0 * 1_000 * 60 * 60 * 24 * 365 * 1000)).millenniums,
            1.nanoseconds into millenniums
        )
        assertEquals((1 / (1_000_000.0 * 60 * 60 * 24 * 365 * 1000)).millenniums, 1.microseconds into millenniums)
        assertEquals((1 / (1000.0 * 60 * 60 * 24 * 365 * 1000)).millenniums, 1.milliseconds into millenniums)
        assertEquals((1 / (60.0 * 60 * 24 * 365 * 1000)).millenniums, 1.seconds into millenniums)
        assertEquals((1 / (60.0 * 24 * 365 * 1000)).millenniums, 1.minutes into millenniums)
        assertEquals((1 / (24.0 * 365 * 1000)).millenniums, 1.hours into millenniums)
        assertEquals((1 / (365.0 * 1000)).millenniums, 1.days into millenniums)
        assertEquals((7 / (365.0 * 1000)).millenniums, 1.weeks into millenniums)
        assertEquals((1 / 1000.0).millenniums, 1.years into millenniums)
        assertEquals((1 / 100.0).millenniums, 1.decades into millenniums)
        assertEquals((1 / 10.0).millenniums, 1.centuries into millenniums)
        assertEquals(1.millenniums, 1.millenniums into millenniums)
    }
}