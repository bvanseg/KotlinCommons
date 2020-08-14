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
