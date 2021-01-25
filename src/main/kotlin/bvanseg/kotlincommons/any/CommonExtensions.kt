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
package bvanseg.kotlincommons.any

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.Optional

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
 * Converts any Object to an [Optional].
 *
 * @author Boston Vanseghi
 * @since 2.0.1
 */
fun <T> T.toOptional(): Optional<T> = Optional.of(this)

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
