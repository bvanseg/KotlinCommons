package bvanseg.kotlincommons.util.ratelimit.event

import bvanseg.kotlincommons.util.project.Experimental
import bvanseg.kotlincommons.util.ratelimit.RateLimiter

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
open class RateLimiterEvent constructor(val rateLimiter: RateLimiter)

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
open class BucketRefillEvent(rateLimiter: RateLimiter): RateLimiterEvent(rateLimiter) {
    class PRE(rateLimiter: RateLimiter): BucketRefillEvent(rateLimiter)
    class POST(rateLimiter: RateLimiter): BucketRefillEvent(rateLimiter)
}

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
open class BucketEmptyEvent(rateLimiter: RateLimiter): RateLimiterEvent(rateLimiter)

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
open class RateLimiterShutdownEvent(rateLimiter: RateLimiter): RateLimiterEvent(rateLimiter) {
    class PRE(rateLimiter: RateLimiter): RateLimiterShutdownEvent(rateLimiter)
    class POST(rateLimiter: RateLimiter): RateLimiterShutdownEvent(rateLimiter)
}