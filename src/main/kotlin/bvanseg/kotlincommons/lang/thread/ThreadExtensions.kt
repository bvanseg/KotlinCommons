package bvanseg.kotlincommons.lang.thread

import bvanseg.kotlincommons.time.api.Khrono
import bvanseg.kotlincommons.time.api.KhronoDate
import bvanseg.kotlincommons.time.api.KhronoDateTime
import bvanseg.kotlincommons.time.api.KhronoTime

fun Thread.sleep(time: Khrono) = Thread.sleep(time.toMillis().toLong())
fun Thread.sleep(time: KhronoTime) = Thread.sleep(time.asMillis.toLong())
fun Thread.sleep(date: KhronoDate) = Thread.sleep(date.asMillis.toLong())
fun Thread.sleep(dateTime: KhronoDateTime) = Thread.sleep(dateTime.asMillis.toLong())