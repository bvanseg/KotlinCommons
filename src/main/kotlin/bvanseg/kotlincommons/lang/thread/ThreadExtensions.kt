package bvanseg.kotlincommons.lang.thread

import bvanseg.kotlincommons.time.api.KTime

fun Thread.sleep(time: KTime) = Thread.sleep(time.toMillis().toLong())