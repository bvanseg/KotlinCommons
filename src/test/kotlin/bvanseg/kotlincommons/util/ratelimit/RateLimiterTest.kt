package bvanseg.kotlincommons.util.ratelimit

import bvanseg.kotlincommons.time.api.seconds
import org.junit.jupiter.api.Disabled
import java.util.concurrent.TimeUnit

@Disabled
class RateLimiterTest {

    val rateLimiter = RateLimiter(
        tokenBucket = TokenBucket(
            tokenLimit = 377,
            refillTime = 60.seconds,
            refillTimeOffset = 3.seconds,
            initUpdate = System.currentTimeMillis()
        ),
        autoStart = true
    )
}