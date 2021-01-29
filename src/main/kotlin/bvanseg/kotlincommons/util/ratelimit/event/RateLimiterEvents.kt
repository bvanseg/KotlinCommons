package bvanseg.kotlincommons.util.ratelimit.event

import bvanseg.kotlincommons.util.project.Experimental
import bvanseg.kotlincommons.util.ratelimit.RateLimiter

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
@Experimental
open class RateLimiterEvent<T> constructor(val rateLimiter: RateLimiter<T>)

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
@Experimental
open class BucketRefillEvent<T>(rateLimiter: RateLimiter<T>): RateLimiterEvent<T>(rateLimiter) {
    class PRE<T>(rateLimiter: RateLimiter<T>): BucketRefillEvent<T>(rateLimiter)
    class POST<T>(rateLimiter: RateLimiter<T>): BucketRefillEvent<T>(rateLimiter)
}

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
@Experimental
open class BucketEmptyEvent<T>(rateLimiter: RateLimiter<T>): RateLimiterEvent<T>(rateLimiter)

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
@Experimental
open class RateLimiterShutdownEvent<T>(rateLimiter: RateLimiter<T>): RateLimiterEvent<T>(rateLimiter) {
    class PRE<T>(rateLimiter: RateLimiter<T>): RateLimiterShutdownEvent<T>(rateLimiter)
    class POST<T>(rateLimiter: RateLimiter<T>): RateLimiterShutdownEvent<T>(rateLimiter)
}