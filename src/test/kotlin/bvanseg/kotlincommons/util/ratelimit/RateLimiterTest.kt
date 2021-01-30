package bvanseg.kotlincommons.util.ratelimit

import org.junit.jupiter.api.Disabled
import java.util.concurrent.TimeUnit

@Disabled
class RateLimiterTest {

    val rateLimiter = RateLimiter(
        tokenBucket = TokenBucket(
            tokenLimit = 377,
            refillTime = TimeUnit.MILLISECONDS.convert(60, TimeUnit.SECONDS),
            refillTimeOffset = TimeUnit.MILLISECONDS.convert(3, TimeUnit.SECONDS),
            initUpdate = System.currentTimeMillis()
        ),
        autoStart = true
    )
}