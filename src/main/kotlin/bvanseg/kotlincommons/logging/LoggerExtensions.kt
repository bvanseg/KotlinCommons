package bvanseg.kotlincommons.logging

import org.slf4j.Logger

fun Logger.debug(any: Any?) = this.debug(any.toString())
fun Logger.error(any: Any?) = this.error(any.toString())
fun Logger.info(any: Any?) = this.info(any.toString())
fun Logger.trace(any: Any?) = this.trace(any.toString())
fun Logger.warn(any: Any?) = this.warn(any.toString())