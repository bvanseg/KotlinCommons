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
fun <T> cache(cacheTime: Khrono, initialValue: T, callback: () -> T) = CacheCallbackDelegate(cacheTime, initialValue, callback)

/**
 * @author Boston Vanseghi
 * @since 2.8.0
 */
fun <T> cache(cacheTime: Khrono, callback: () -> T) = CacheCallbackDelegate(cacheTime, callback(), callback)