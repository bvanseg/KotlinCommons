package bvanseg.kotlincommons.any

import bvanseg.kotlincommons.observable.Observable
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Allows any class to get a logger of itself.
 */
fun Any.getLogger(): Logger = LoggerFactory.getLogger(this::class.java)

/**
 * Converts any Object to an [Observable].
 *
 * @author Boston Vanseghi
 * @since 2.0.1
 */
fun Any.toObservable(): Observable<Any> = Observable(this)

/**
 * Converts any Object to an [Optional].
 *
 * @author Boston Vanseghi
 * @since 2.0.1
 */
fun Any.toOptional(): Optional<Any> = Optional.of(this)