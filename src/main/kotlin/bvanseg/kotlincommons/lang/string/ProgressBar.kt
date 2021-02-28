package bvanseg.kotlincommons.lang.string

import bvanseg.kotlincommons.lang.checks.Checks
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
        Checks.isFinite(currentProgress, "currentProgress")
        Checks.isWholeNumber(currentProgress, "currentProgress")
        Checks.isFinite(maxProgress, "maxProgress")
        Checks.isPositive(maxProgress, "maxProgress")

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