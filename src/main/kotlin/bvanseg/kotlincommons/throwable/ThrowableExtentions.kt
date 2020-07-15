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
 *
 * @author bright_spark
 * @since 2.2.5
 */
fun Throwable.analyse() = ExceptionAnalysis(this)
