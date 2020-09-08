package bvanseg.kotlincommons.time

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit

class StopwatchTest {

    @Test
    fun testDuration() {
        val watch = Stopwatch()

        watch.start()
        Thread.sleep(1000)
        watch.stop()
        assertTrue(watch.getElapsedTime(TimeUnit.MILLISECONDS) >= 999)
    }

    @Test
    fun testPausing() {
        val watch = Stopwatch()

        watch.start()
        Thread.sleep(1000)
        watch.pause()
        Thread.sleep(1000)
        watch.resume()
        Thread.sleep(1000)
        watch.stop()

        assertTrue(watch.getElapsedTime(TimeUnit.MILLISECONDS) < 2500)
        assertTrue(watch.getElapsedTime(TimeUnit.MILLISECONDS) >= 1999)
    }
}