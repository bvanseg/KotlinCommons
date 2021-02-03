/*
 * MIT License
 *
 * Copyright (c) 2021 Boston Vanseghi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package bvanseg.kotlincommons.time.api.performer

import bvanseg.kotlincommons.time.api.Khrono
import bvanseg.kotlincommons.time.api.KhronoDate
import bvanseg.kotlincommons.time.api.KhronoDateTime
import bvanseg.kotlincommons.time.api.KhronoTime
import bvanseg.kotlincommons.time.api.milliseconds

/**
 * @author Boston Vanseghi
 * @since 2.7.3
 */
fun every(timeInMillis: Long, counterDrift: Boolean = false, cb: (KhronoPerformer) -> Unit): KhronoPerformer =
    every(timeInMillis.milliseconds, counterDrift, cb)

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
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