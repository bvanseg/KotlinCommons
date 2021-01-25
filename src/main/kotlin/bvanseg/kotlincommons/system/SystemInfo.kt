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
package bvanseg.kotlincommons.system

import java.lang.management.ManagementFactory
import javax.management.Attribute
import javax.management.ObjectName

/**
 * Able to grab various system information, such as usage of memory by the program.
 *
 * @author bright_spark
 * @since 1.0.0
 */
// TODO: Move to KotlinCommons object.
object SystemInfo {

    /**
     * @return The maximum amount of memory the Java Runtime Environment will use in bytes.
     */
    val maxMemory: Long
        get() = Runtime.getRuntime().maxMemory()

    /**
     * @return The memory used by this program in bytes.
     */
    val usedMemory: Long
        get() = totalMemory - freeMemory

    /**
     * @return The total memory available to the Java Runtime Environment in bytes.
     * As opposed to [maxMemory], this method may return a varying number according to the host environment (OS, system memory, etc.).
     */
    val totalMemory: Long
        get() = Runtime.getRuntime().totalMemory()

    /**
     * @return The amount of free memory the Java Runtime Environment has in bytes.
     */
    val freeMemory: Long
        get() = Runtime.getRuntime().freeMemory()

    /**
     * @return A rough approximation of how much this program is straining the CPU.
     */
    // usually takes a couple of seconds before we get real values
    // returns a percentage value with 1 decimal point precision
    val processCpuLoad: Double
        get() {
            try {
                val mbs = ManagementFactory.getPlatformMBeanServer()
                val name = ObjectName.getInstance("java.lang:type=OperatingSystem")
                val list = mbs.getAttributes(name, arrayOf("ProcessCpuLoad"))

                if (list.isEmpty())
                    return Double.NaN

                val att = list[0] as Attribute
                val value = att.value as Double
                return if (value == -1.0) Double.NaN else (value * 1000).toInt() / 10.0
            } catch (e: Exception) {
                e.printStackTrace()
                return 0.0
            }
        }

    /**
     * @return The Java version that is currently being used by this program.
     */
    val runtimeJavaVersion: String
        get() = Runtime.version().toString()

    /**
     * @return The Java version the system has as its default.
     */
    val systemJavaVersion: String
        get() = System.getProperty("java.version")

    /**
     * @return The vendor of the current JRE.
     */
    val javaVendor: String
        get() = System.getProperty("java.vendor")

    /**
     * @return The name of the Operating System this program is running under.
     */
    val osName: String
        get() = System.getProperty("os.name")

    /**
     * @return The architecture of the Operating System.
     */
    val osArchitecture: String
        get() = System.getProperty("os.arch")

    /**
     * @return The Operating System's version.
     */
    val osVersion: String
        get() = System.getProperty("os.version")
}