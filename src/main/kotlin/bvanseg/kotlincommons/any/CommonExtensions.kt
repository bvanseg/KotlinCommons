package bvanseg.kotlincommons.any

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Allows any class to get a logger of itself.
 */
fun Any.getLogger(): Logger = LoggerFactory.getLogger(this::class.java)

/**
 * Converts any Object to an [Optional].
 *
 * @author Boston Vanseghi
 * @since 2.0.1
 */
fun <T> T.toOptional(): Optional<T> = Optional.of(this)