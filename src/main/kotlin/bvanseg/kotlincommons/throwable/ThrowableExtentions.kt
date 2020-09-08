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

import java.io.PrintWriter
import java.io.StringWriter

/**
 * Returns this [Throwable] as a String with the error and stack trace.
 *
 * @author bright_spark
 * @since 2.2.5
 */
fun Throwable.printToString(): String = StringWriter().use { sw ->
    PrintWriter(sw).use { pw ->
        this.printStackTrace(pw)
        sw.toString()
    }
}

/**
 * Returns this [Throwable] as an [ExceptionAnalysis] which provides a breakdown of the exception.
 * Can use [bvanseg.kotlincommons.any.packagePath] to easily get the package path of the current class.
 *
 * @author bright_spark
 * @since 2.2.5
 */
fun Throwable.analyse(rootPackage: String) = ExceptionAnalysis(this, rootPackage)
