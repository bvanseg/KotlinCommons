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
package bvanseg.kotlincommons.time

/**
 * A tick-based timing class that can be used to create tick-based systems, which are commonly used in video games.
 *
 * @author Ocelot5836
 * @since 1.0.1
 */
class Timer(private val ticksPerSecond: Float) {

    /**
     * The elapsed ticks that have passed.
     */
    @Volatile
    var elapsedTicks: Int = 0

    /**
     * Gets the ticks passed as a floating-point number. Allows for interpolation.
     */
    var partialTicks: Float = 0.toFloat()

    /**
     * The total number of partial ticks that have passed.
     */
    var elapsedPartialTicks: Float = 0.toFloat()

    private var lastSyncSysClock: Long = 0

    /**
     * How long a tick is compared to a millisecond.
     */
    val tickLength: Float = 1000.0f / ticksPerSecond

    init {
        this.lastSyncSysClock = System.currentTimeMillis()
    }

    /**
     * Updates the timer, which updates the tick values within the class as necessary.
     */
    fun updateTimer() {
        val i = System.currentTimeMillis()
        this.elapsedPartialTicks = (i - this.lastSyncSysClock) / this.tickLength
        this.lastSyncSysClock = i
        this.partialTicks += this.elapsedPartialTicks
        this.elapsedTicks = this.partialTicks.toInt()
        this.partialTicks -= this.elapsedTicks.toFloat()
    }
}