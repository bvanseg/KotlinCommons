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
package bvanseg.kotlincommons.util.delegate

import bvanseg.kotlincommons.util.comparable.clamp
import kotlin.reflect.KProperty

/**
 * A delegator class that clamps a value as it is set.
 *
 * @param value The initial value to clamp.
 * @param lowerBound The lower boundary for value. This bound is exclusive.
 * @param upperBound The upper boundary for value. This bound is exclusive.
 *
 * @author Boston Vanseghi
 * @since 2.9.1
 */
class ClampingDelegate<T : Comparable<T>>(value: T, private val lowerBound: T, private val upperBound: T) {

    private var clampedValue: T = clamp(value, lowerBound, upperBound)

    @Synchronized
    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) {
        clampedValue = clamp(newValue, lowerBound, upperBound)
    }

    @Synchronized
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = clampedValue
}