package bvanseg.kotlincommons.timedate

import bvanseg.kotlincommons.timedate.transformer.into
import bvanseg.kotlincommons.timedate.transformer.until
import org.junit.jupiter.api.Test
import java.time.Instant

class TestPerform {

    @Test
    fun testBlockingPerform() {
        println(now + 1000.years)
        println(yesterday)
        println(tomorrow)
        println(now isBefore tomorrow)
        println(now isBefore (10.minutes from now))

        println(now until (1.minutes from now) into seconds)

        val start = now
//    sleep(1.minutes)
        println(start)
        /*
            now = 09:30:13.2000
            09:30:13
            09:31:13
            09:32:00
            09:33:00
         */
        (start until (1.hours from start)
                every ((30.seconds.pronto)
                waitUntil (1.minutes from now)))
            .perform {
                println("Hello, world! - ${Instant.now()}")
            }

//    val job = (start until (10.minutes from start)
//            every ((1.minutes.exactly)
//            waitUntil (1.minutes.exactly)
//            starting (1.minutes from now)))
//        .performAsync {
//            delay(1000)
//            println("Hello, world! - ${Instant.now()}")
//        }
//    job.join()

        println("Reached end of function.")
    }
}