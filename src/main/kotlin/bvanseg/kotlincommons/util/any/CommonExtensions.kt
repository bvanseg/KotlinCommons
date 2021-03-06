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
package bvanseg.kotlincommons.util.any

import bvanseg.kotlincommons.time.api.Khrono
import bvanseg.kotlincommons.time.api.KhronoDate
import bvanseg.kotlincommons.time.api.KhronoDateTime
import bvanseg.kotlincommons.time.api.KhronoTime
import java.util.Optional

/**
 * Converts any Object to an [Optional].
 *
 * @author Boston Vanseghi
 * @since 2.0.1
 */
fun <T : Any> T.toOptional(): Optional<T> = Optional.of(this)

/**
 * Gets the package path of the current class, optionally with a [depth] limit.
 * A [depth] of 0 or less will return the whole path.
 *
 * @param depth The depth of traversal into the package path.
 *
 * @author bright_spark
 * @since 2.4.0
 */
fun Any.packagePath(depth: Int = 0): String = this::class.qualifiedName!!.substringBeforeLast('.').let { path ->
    if (depth <= 0) path else path.split('.').take(depth).joinToString(".")
}

/**
 * Synchronizes [this].
 *
 * @param callback The callback to execute in a synchronized scope.
 *
 * @return The last construct at the end of the synchronized scope as type [T].
 *
 * @author Boston Vanseghi
 * @since 2.7.0
 */
inline fun <T : Any, R> T.sync(callback: () -> R) = synchronized(this) {
    callback()
}

suspend fun delay(time: Khrono) = kotlinx.coroutines.delay(time.toMillis().toLong())
suspend fun delay(time: KhronoTime) = kotlinx.coroutines.delay(time.asMillis.toLong())
suspend fun delay(time: KhronoDate) = kotlinx.coroutines.delay(time.asMillis.toLong())
suspend fun delay(time: KhronoDateTime) = kotlinx.coroutines.delay(time.asMillis.toLong())
