package bvanseg.kotlincommons.timedate

interface TimeContext{
    /**
     * This will be used for coercions to hours.
     */
    val asHour: Long
    /**
     * This will be used for coercions to minutes.
     */
    val asMinute: Long
    /**
     * This will be used for coercions to seconds.
     */
    val asSeconds: Long
    /**
     * This will be used for coercions to nanoseconds.
     */
    val asNano: Long

    /**
     * This will be used for coercions to milliseconds
     */
    val asMillis: Long

    val pronto: TimePerformer
    val exactly: TimePerformer
}