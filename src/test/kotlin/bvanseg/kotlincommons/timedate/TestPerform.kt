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
        println(start)
        /*
            now = 09:30:13.2000
            09:30:13
            09:31:13
            09:32:00
            09:33:00
         */

//        (start until (tomorrow) // Execute forever.
//            every ((1.minutes.exactly) at (0.seconds)) // execute only on the mark of 1 minute intervals.
//            )
//            .performAsync {
//                println("Hello, is it me you're looking for?")
//            }

//        (start until (5.seconds from start) // Execute for 5 seconds.
//                every ((1.seconds.exactly) // execute only on the mark of 1 second intervals.
//                waitUntil (0.seconds) // Execute on every 1st second.
//                )) // 120 second delay.
//            .perform {
//                println("Hello, world! - ${Instant.now()}") // action
//            }
//        ((start) until (10.years from start)
//                every ((1.minutes.exactly) at (0.seconds) starting (60.seconds from start)) // execute only on the mark of 1 minute intervals.
//                ) perform {
//                    println("Hello, world! - ${Instant.now()}") // action
//                }

        println("Reached end of function.")
    }
}

/* PERFORMANCE USE-CASE EXAMPLES */

/*

    EXAMPLE 0: WORKS
        - Prints "Hello, world! - currentInstantNowHere" to the console every 30 seconds for 2 cycles.
        - No delay, fires immediately.

    (start until (1.minutes from start)
                every (30.seconds))
            .perform {
                println("Hello, world! - ${Instant.now()}")
            }

    EXAMPLE 1: WORKS
        - Prints "Hello, world! - currentInstantNowHere" to the console every 30 seconds for 2 cycles.
        - No delay, fires immediately.
        - Differs from Example 0 in that, we execute only on 30-second marks. so 30, 0, 30, 0, and so on. This is
        far more precise than without exactly (which is the alternative known as "pronto".

    (start until (1.minutes from start)
                every (30.seconds.exactly))
            .perform {
                println("Hello, world! - ${Instant.now()}")
            }

    EXAMPLE 2: WORKS
        - This works just like Example 1, except it will wait until where the minute of the hour is equal to 1.
            - This could involve waiting for at most an hour to roll around before minutes == 1.
            - This is multi-cyclical. The first cycle is the every, the second cycle is the waitUntil. In English, this
            loop will occur for a total of 1 minute, split into 2 cycles, looped at the beginning of every 2nd minute.
            (for the 1st minute, it would be 0.minutes)

    (start until (1.minutes from start)
                every ((30.seconds.exactly)
                waitUntil (1.minutes)))
            .perform {
                println("Hello, world! - ${Instant.now()}")
            }

    EXAMPLE 3: WORKS
        - This works the same as Example 1, with an initial delay of 15 seconds.

    (start until (1.minutes from start)
                every ((30.seconds.exactly)
                starting (15.seconds from now)))
            .perform {
                println("Hello, world! - ${Instant.now()}")
            }

    EXAMPLE 4: WORKS -- NEEDS IMPLEMENTATION!!!!!!!!!!!!!!!!!!
        - This works just like example 3, the only difference is now the delay will wait until the first 15 seconds in
        the minute, not 15 seconds from where-ever we start.

    (start until (1.minutes from start)
                every ((30.seconds.exactly)
                starting (15.seconds.exactly from now))) // DIFFERENT, IS NOW EXACTLY.
            .perform {
                println("Hello, world! - ${Instant.now()}")
            }

    EXAMPLE 5: DOESN'T WORK
        - Does not properly run due to waitUntil failing with the given UnitBasedTimeContainer.
        - Currently, UnitBasedTimeContainer is treated in two use-cases: 1 use-case for an offset of a LocalDateTimeContainer,
        and also as a simple data holder for unit(s). To solve this issue, we should specialize UnitBasedTimeContainer into
        a new type that is yielded from applying a UnitBasedTimeContainer offset to a LocalDateTimeContainer variable.

    (start until (1.minutes from start)
                every ((30.seconds.exactly)
                waitUntil (1.minutes from now))) // POINT OF FAILURE
            .perform {
                println("Hello, world! - ${Instant.now()}")
            }

    CONCLUSION:
        - waitUntil should only be used with UnitBasedTimeContainers only.
        - Additionally, waitUntil should now be used for chaining callbacks. It is far more useful as a way to execute
        a task every nth unit, such as every 1st minute, or every 1st day, or even every 1st hour.
            - Expanding upon this, waitUntil should be renamed to appropriately match this new usage.
                - Whatever the new name should be, waitUntil should serve as a semantic delegation to starting, meaning
                it will perform the same function as starting, just with a different word.
 */