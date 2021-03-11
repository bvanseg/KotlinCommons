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

import kotlin.jvm.Throws

/**
 * @author Boston Vanseghi
 * @since 2.9.7
 */
class Check<in T> private constructor(private val checkMessage: String, private val checkCallback: (T) -> Boolean) {

    companion object {
        fun <T> create(checkMessage: String = "Check failed for %s: '%s'", checkCallback: (T) -> Boolean): Check<T> = Check(checkMessage, checkCallback)

        fun <T> all(value: T, valueName: String, vararg checks: Check<T>) {
            checks.forEach { it.check(value, valueName) }
        }
    }

    @Throws(CheckException::class)
    fun check(value: T, valueName: String = "value") {
        if (!checkCallback(value)) throw CheckException(checkMessage.format(valueName, value))
    }

    @Throws(CheckException::class)
    fun checkOpposite(value: T, valueName: String = "value") {
        if (checkCallback(value)) throw CheckException(checkMessage.format(valueName, value))
    }
}