package bvanseg.kotlincommons.util.delegate

import bvanseg.kotlincommons.time.api.Khrono
import bvanseg.kotlincommons.util.any.sync
import kotlin.reflect.KProperty

/**
 * Stores the cached value for the given amount of [khrono] time. Attempting to access the value after expiration
 * will return the reset value (or null if a nullable value).
 *
 * @param khrono The [Khrono] amount of time until property expiration.
 * @param initialValue The initial value to fill the cache with.
 * @param callback The callback to execute to regenerate a new value.
 *
 * @author Boston Vanseghi
 * @since 2.8.0
 */
class CacheCallbackDelegate<T>(val khrono: Khrono, initialValue: T, private val callback: () -> T) {

    private var cachedValue: T = initialValue
    private var expireTime = System.currentTimeMillis() + khrono.toMillis().toLong()

    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) = synchronized(this) {
        expireTime = System.currentTimeMillis() + khrono.toMillis().toLong()
        cachedValue = newValue
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = synchronized(this) {
        if (System.currentTimeMillis() > expireTime) {
            cachedValue = callback()
        }

        return cachedValue
    }
}