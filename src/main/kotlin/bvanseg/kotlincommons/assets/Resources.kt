package bvanseg.kotlincommons.assets

import bvanseg.kotlincommons.system.runOnExit
import io.github.classgraph.ClassGraph
import io.github.classgraph.ScanResult

/**
 * Used to access resources with help from [ClassGraph].
 *
 * @author bright_spark
 * @since 2.0.4
 */
object Resources {

    init {
        runOnExit { close() }
    }

    private var initialised = false
    val scan: ScanResult by lazy {
        initialised = true
        return@lazy ClassGraph().enableAllInfo().scan()
    }

    private fun close() = if (initialised) scan.close() else Unit
}