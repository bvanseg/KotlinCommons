package bvanseg.kotlincommons.util.project

/**
 * Classifies a construct as experimental. Experimental constructs should be used with caution.
 *
 * @author Boston Vanseghi
 * @since 2.7.0
 */
@RequiresOptIn
annotation class Experimental(val warning: String = "")
