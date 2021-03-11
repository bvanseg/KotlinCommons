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
package bvanseg.kotlincommons.lang.check

import bvanseg.kotlincommons.math.isEven
import bvanseg.kotlincommons.math.isNegative
import bvanseg.kotlincommons.math.isOdd
import bvanseg.kotlincommons.math.isPositive
import kotlin.reflect.KClass

/**
 * Utility object that performs short common checks.
 *
 * @author Boston Vanseghi
 * @since 2.9.7
 */
object Checks {

    val isCompanion by lazy { Check.create<Any> { it::class.isCompanion } }
    val isDataClass by lazy { Check.create<KClass<*>> { it.isData } }

    val isInstanceOfDataClass by lazy { Check.create<Any> { it::class.isData } }

    val isNull by lazy { Check.create<Any?> { it == null } }
    val isNotNull by lazy { Check.create<Any?> { it != null } }

    val isTrue by lazy { Check.create<Boolean> { it } }
    val isFalse by lazy { Check.create<Boolean> { !it } }

    val isPositive by lazy { Check.create<Number> { it.isPositive() } }
    val isNegative by lazy { Check.create<Number> { it.isNegative() } }
    val isEven by lazy { Check.create<Number> { it.isEven() } }
    val isOdd by lazy { Check.create<Number> { it.isOdd() } }
    val isWholeNumber by lazy { Check.create<Number> { it.toDouble() >= 0 } }
    val isFinite by lazy { Check.create<Double> { it.isFinite() } }
    val isInfinite by lazy { Check.create<Double> { it.isInfinite() } }
    val isNaN by lazy { Check.create<Double> { it.isNaN() } }

    val notBlank by lazy { Check.create<String>("Check failed for %s: '%s' is blank.") { it.isNotBlank() } }
    val noWhitespace by lazy { Check.create<String>("Check failed for %s: '%s' contains whitespace.") { !it.contains(" ") } }
}