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
package bvanseg.kotlincommons.string

import java.net.URI
import java.net.URL
import java.util.Base64

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
 * Attempts to create a [URI] from a [String]. If a [URI] can't be created from the given [String], returns null.
 *
 * @return a [URI] or null if the [String] is not a valid [URI].
 *
 * @author Boston Vanseghi
 * @since 2.7.0
 */
fun String.toURIOrNull(): URI? = try { URI.create(this) } catch(e: Exception) { null }

/**
 * Attempts to create a [URL] from a [String]. If a [URL] can't be created from the given [String], returns null.
 *
 * @return a [URL] or null if the [String] is not a valid [URL].
 *
 * @author Boston Vanseghi
 * @since 2.7.0
 */
fun String.toURLOrNull(): URL? = try { URL(this) } catch (e: Exception) { null }

/**
 * Encodes a String to [Base64].
 *
 * @author Boston Vanseghi
 * @since 2.6.0
 */
fun String.toBase64(): String = Base64.getEncoder().encodeToString(this.toByteArray(Charsets.US_ASCII))

/**
 * Decodes a plain [ByteArray] from a [Base64]-encoded [String].
 *
 * @author Boston Vanseghi
 * @since 2.7.0
 */
fun String.fromBase64(): ByteArray = Base64.getDecoder().decode(this)

/**
 * Returns a truncated string containing the first [n] characters from this string, or the entire string if this string
 * is shorter.
 *
 * @author bright_spark
 * @since 2.2.4
 */
fun String.truncate(n: Int, truncated: String = "..."): String =
    if (this.length <= n) this else take(n - truncated.length) + truncated
