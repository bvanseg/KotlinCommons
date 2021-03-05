/*
 * MIT License
 *
 * Copyright (c) 2021 Boston Vanseghi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package bvanseg.kotlincommons.util.ratelimit.event

import bvanseg.kotlincommons.util.ratelimit.RateLimiter

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
@Deprecated("To be removed in KotlinCommons 2.10.0")
open class RateLimiterEvent constructor(val rateLimiter: RateLimiter)

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
@Deprecated("To be removed in KotlinCommons 2.10.0")
open class BucketRefillEvent(rateLimiter: RateLimiter) : RateLimiterEvent(rateLimiter) {
    @Deprecated("To be removed in KotlinCommons 2.10.0")
    class PRE(rateLimiter: RateLimiter) : BucketRefillEvent(rateLimiter)
    @Deprecated("To be removed in KotlinCommons 2.10.0")
    class POST(rateLimiter: RateLimiter) : BucketRefillEvent(rateLimiter)
}

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
@Deprecated("To be removed in KotlinCommons 2.10.0")
open class BucketEmptyEvent(rateLimiter: RateLimiter) : RateLimiterEvent(rateLimiter)

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
@Deprecated("To be removed in KotlinCommons 2.10.0")
open class RateLimiterShutdownEvent(rateLimiter: RateLimiter) : RateLimiterEvent(rateLimiter) {
    @Deprecated("To be removed in KotlinCommons 2.10.0")
    class PRE(rateLimiter: RateLimiter) : RateLimiterShutdownEvent(rateLimiter)
    @Deprecated("To be removed in KotlinCommons 2.10.0")
    class POST(rateLimiter: RateLimiter) : RateLimiterShutdownEvent(rateLimiter)
}