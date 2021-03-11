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
package bvanseg.kotlincommons.util.delegate

import bvanseg.kotlincommons.time.api.Khrono

/**
 * @author Boston Vanseghi
 * @since 2.8.0
 */
fun <T> nullableCache(cacheTime: Khrono, initialValue: T? = null) = cache(cacheTime, initialValue, null)

/**
 * @author Boston Vanseghi
 * @since 2.8.0
 */
fun <T> cache(cacheTime: Khrono, initialValue: T, resetValue: T) = CacheDelegate(cacheTime, initialValue, resetValue)

/**
 * @author Boston Vanseghi
 * @since 2.8.0
 */
fun <T> cache(cacheTime: Khrono, initialValue: T, callback: () -> T) =
    CacheCallbackDelegate(cacheTime, initialValue, callback)

/**
 * @author Boston Vanseghi
 * @since 2.8.0
 */
fun <T> cache(cacheTime: Khrono, callback: () -> T) = CacheCallbackDelegate(cacheTime, callback(), callback)

/**
 * @author Boston Vanseghi
 * @since 2.9.1
 */
fun <T : Comparable<T>> clamping(value: T, lowerBound: T, upperBound: T) =
    ClampingDelegate(value, lowerBound, upperBound)

/**
 * @author Boston Vanseghi
 * @since 2.9.7
 */
fun <T : Comparable<T>> greatest(value: T) = GreatestDelegate(value)

/**
 * @author Boston Vanseghi
 * @since 2.9.7
 */
fun <T : Comparable<T>> least(value: T? = null) = LeastDelegate(value)