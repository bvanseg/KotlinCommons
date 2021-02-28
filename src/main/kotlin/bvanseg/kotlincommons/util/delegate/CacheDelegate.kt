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
import kotlin.reflect.KProperty

/**
 * Stores the cached value for the given amount of [khrono] time. Attempting to access the value after expiration
 * will return the reset value (or null if a nullable value).
 *
 * @param khrono The [Khrono] amount of time until property expiration.
 * @param initialValue The initial value to fill the cache with.
 * @param resetValue The value to reset the property to once it expires.
 *
 * @author Boston Vanseghi
 * @since 2.8.0
 */
class CacheDelegate<T>(val khrono: Khrono, initialValue: T, private val resetValue: T) {

    private var cachedValue: T = initialValue
    private var expireTime = System.currentTimeMillis() + khrono.toMillis().toLong()

    @Synchronized
    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) {
        expireTime = System.currentTimeMillis() + khrono.toMillis().toLong()
        cachedValue = newValue
    }

    @Synchronized
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (System.currentTimeMillis() > expireTime) {
            cachedValue = resetValue
        }

        return cachedValue
    }
}