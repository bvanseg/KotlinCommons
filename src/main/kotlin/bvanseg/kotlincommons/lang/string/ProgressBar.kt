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
package bvanseg.kotlincommons.lang.string

import bvanseg.kotlincommons.lang.check.Check
import bvanseg.kotlincommons.lang.check.Checks
import bvanseg.kotlincommons.util.delegate.clamping

/**
 * Simple utility class that creates a progress bar as a [String].
 *
 * @param head The left-most character starting the bar.
 * @param tail The right-most character ending the bar.
 * @param fullBody The character representing progress.
 * @param emptyBody The character representing the remaining amount to go until complete.
 * @param barLength The number of characters the bar spans. For the actual full character length including the head and
 * the tail, this number should have 2 added to it.
 *
 * @author Boston Vanseghi
 * @since 2.9.7
 */
class ProgressBar(
    private val head: String = "<",
    private val tail: String = ">",
    private val fullBody: String = "=",
    private val emptyBody: String = "-",
    barLength: Int = 10
) {

    private val barLength: Int by clamping(barLength, 0, Int.MAX_VALUE)

    /**
     * Generates a progress bar given a current progress and a max progress value.
     *
     * @param currentProgress The current progress out of the total.
     * @param maxProgress The maximum progress until the bar is complete. Must not be 0.
     *
     * @return The bar as a [String] generated from the [currentProgress] progress and the [maxProgress].
     */
    fun getBarFor(currentProgress: Double, maxProgress: Double): String {
        Check.all(currentProgress, "currentProgress", Checks.isFinite, Checks.isWholeNumber)
        Check.all(maxProgress, "maxProgress", Checks.isFinite, Checks.isPositive)

        val builder = StringBuilder()
        builder.append(head)
        val progress = (currentProgress / maxProgress) * barLength
        for (i in 0 until barLength) {
            if (i < progress) {
                builder.append(fullBody)
            } else {
                builder.append(emptyBody)
            }
        }
        builder.append(tail)

        return builder.toString()
    }
}