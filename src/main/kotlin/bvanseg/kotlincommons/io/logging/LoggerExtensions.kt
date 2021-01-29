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
package bvanseg.kotlincommons.io.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Allows any class to get a logger of itself.
 *
 * @author Boston Vanseghi
 */
fun Any.getLogger(): Logger {
    val klass = this::class
    val clazz = klass.java

    return if (klass.isCompanion) {
        LoggerFactory.getLogger(clazz.declaringClass)
    } else {
        LoggerFactory.getLogger(clazz)
    }
}

/**
 * Logs an object of any type as a [String].
 *
 * @param any The object to log.
 *
 * @author Boston Vanseghi
 */
fun Logger.debug(any: Any?) = this.debug("{}", any.toString())

/**
 * Logs an object of any type as a [String].
 *
 * @param any The object to log.
 *
 * @author Boston Vanseghi
 */
fun Logger.error(any: Any?) = this.error("{}", any.toString())

/**
 * Logs an object of any type as a [String].
 *
 * @param any The object to log.
 *
 * @author Boston Vanseghi
 */
fun Logger.info(any: Any?) = this.info("{}", any.toString())

/**
 * Logs an object of any type as a [String].
 *
 * @param any The object to log.
 *
 * @author Boston Vanseghi
 */
fun Logger.trace(any: Any?) = this.trace("{}", any.toString())

/**
 * Logs an object of any type as a [String].
 *
 * @param any The object to log.
 *
 * @author Boston Vanseghi
 */
fun Logger.warn(any: Any?) = this.warn("{}", any.toString())

inline fun Logger.info(callback: () -> Any) {
    if (this.isInfoEnabled) this.info(callback())
}

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
inline fun Logger.warn(callback: () -> Any) {
    if (this.isWarnEnabled) this.warn(callback())
}

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
inline fun Logger.error(callback: () -> Any) {
    if (this.isErrorEnabled) this.error(callback())
}

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
inline fun Logger.error(e: Throwable, callback: (Throwable) -> String) {
    if (this.isErrorEnabled) this.error(callback(e), e)
}

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
inline fun Logger.debug(callback: () -> Any) {
    if (this.isDebugEnabled) this.debug(callback())
}

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
inline fun Logger.trace(callback: () -> Any) {
    if (this.isTraceEnabled) this.trace(callback())
}