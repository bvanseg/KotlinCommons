/*
 * MIT License
 *
 * Copyright (c) 2019 Boston Vanseghi
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
package bvanseg.kotlincommons.string

import java.net.URI
import java.net.URL

fun List<String>.joinStrings(startIndex: Int = 0, endIndex: Int = -1): String {
    val sb = StringBuilder()
    val start = kotlin.math.max(startIndex, 0)
    val end = if (endIndex < 0) this.size else kotlin.math.min(endIndex, this.size)
    if (start > end)
        throw IllegalArgumentException("Start index ($start) must not be greater than the end index ($end)")
    (start until end).forEach {
        if (it > start)
            sb.append(" ")
        sb.append(this[it])
    }
    return sb.toString()
}

fun String.remove(vararg strings: String): String {
    var str = this
    strings.forEach {
        str = this.replace(it, "")
    }
    return str
}

/**
 * Creates a [URI] from a [String].
 *
 * @author Boston Vanseghi
 * @since 2.1.6
 */
fun String.toURI(): URI = URI.create(this)

/**
 * Creates a [URL] from a [String].
 *
 * @author Boston Vanseghi
 * @since 2.1.6
 */
fun String.toURL(): URL = URL(this)

/**
 * Returns a truncated string containing the first [n] characters from this string, or the entire string if this string
 * is shorter.
 *
 * @author bright_spark
 * @since 2.2.4
 */
fun String.truncate(n: Int, truncated: String = "..."): String =
    if (this.length <= n) this else take(n - truncated.length) + truncated
