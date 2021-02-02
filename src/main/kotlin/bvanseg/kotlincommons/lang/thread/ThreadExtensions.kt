package bvanseg.kotlincommons.lang.thread

import bvanseg.kotlincommons.time.api.Khrono

fun Thread.sleep(time: Khrono) = Thread.sleep(time.toMillis().toLong())