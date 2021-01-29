package bvanseg.kotlincommons.time.api

import bvanseg.kotlincommons.util.project.Experimental

/**
 * Represents the concept of a time unit without a value.
 *
 * @author Boston Vanseghi
 * @since 2.7.0
 */
open class KTimeBase(open val unit: KTimeUnit)