package bvanseg.kotlincommons.time.api

import bvanseg.kotlincommons.time.api.performer.KhronoPerformer

fun every(timeInMillis: Long, counterDrift: Boolean = false, cb: (KhronoPerformer) -> Unit): KhronoPerformer =
    every(timeInMillis.milliseconds, counterDrift, cb)

fun every(frequency: Khrono, counterDrift: Boolean = false, cb: (KhronoPerformer) -> Unit): KhronoPerformer =
    KhronoPerformer(frequency, cb, counterDrift)