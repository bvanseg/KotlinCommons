/*
 * MIT License
 *
 * Copyright (c) 2020 Boston Vanseghi
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
package bvanseg.kotlincommons.throwable

/**
 * Takes a [Throwable] and creates a simple analysis of the exception.
 *
 * @author bright_spark
 * @since 2.2.5
 */
class ExceptionAnalysis(val exception: Throwable, val rootPackage: String) {
    /** The exception message. */
    val message = exception.toString()
    /** A list of each exception that causes the [exception] */
    val cause: List<String>
    /** The root exception that causes the [exception] */
    val rootCause: Throwable

    init {
        cause = mutableListOf<String>()
            .apply {
                var e = exception
                while (e.cause != null) {
                    e = e.cause!!
                    add(e.toString())
                }
                rootCause = e
            }
            .toList()
    }

    /**
     * The line that potentially caused the [exception].
     * This is discovered by analysing package names that match the [rootPackage].
     * */
    val problematicLine =
        rootCause.stackTrace
            .firstOrNull { it.className.startsWith(rootPackage) && it.lineNumber > 0 }
            ?.toString()
            ?: exception.stackTrace[0].toString()

    /**
     * Returns the [exception] as a String.
     * @see [Throwable.printToString]
     */
    fun exceptionToString(): String = exception.printToString()
}
