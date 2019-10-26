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