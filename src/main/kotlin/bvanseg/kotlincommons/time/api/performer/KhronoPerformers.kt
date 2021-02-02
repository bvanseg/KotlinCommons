package bvanseg.kotlincommons.time.api.performer

import bvanseg.kotlincommons.time.api.Khrono
import bvanseg.kotlincommons.time.api.KhronoDate
import bvanseg.kotlincommons.time.api.KhronoDateTime
import bvanseg.kotlincommons.time.api.KhronoTime
import bvanseg.kotlincommons.time.api.milliseconds

fun every(timeInMillis: Long, counterDrift: Boolean = false, cb: (KhronoPerformer) -> Unit): KhronoPerformer =
    every(timeInMillis.milliseconds, counterDrift, cb)

fun every(frequency: Khrono, counterDrift: Boolean = false, cb: (KhronoPerformer) -> Unit): KhronoPerformer =
    KhronoPerformer(frequency, cb, counterDrift)

/**
 * @author Boston Vanseghi
 * @since 2.8.0
 */
fun every(frequency: KhronoTime, counterDrift: Boolean = false, cb: (KhronoPerformer) -> Unit): KhronoPerformer =
    every(frequency.asMillis.toLong(), counterDrift, cb)

/**
 * @author Boston Vanseghi
 * @since 2.8.0
 */
fun every(frequency: KhronoDate, counterDrift: Boolean = false, cb: (KhronoPerformer) -> Unit): KhronoPerformer =
    every(frequency.asMillis.toLong(), counterDrift, cb)

/**
 * @author Boston Vanseghi
 * @since 2.8.0
 */
fun every(frequency: KhronoDateTime, counterDrift: Boolean = false, cb: (KhronoPerformer) -> Unit): KhronoPerformer =
    every(frequency.asMillis.toLong(), counterDrift, cb)