package bvanseg.kotlincommons.any

import bvanseg.kotlincommons.observable.Observable
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Allows any class to get a logger of itself.
 */
fun Any.getLogger(): Logger = LoggerFactory.getLogger(this::class.java)


fun Any.toObservable(): Observable<Any> = Observable(this)