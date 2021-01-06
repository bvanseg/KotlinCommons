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
package bvanseg.kotlincommons.number

/**
 * Hashes any given data to a unique hash code.
 *
 * @author Boston Vanseghi
 * @since 2.1.1
 */
class HashCodeBuilder(clazz: Any) {

    init {
        this.append(clazz::class.simpleName)
    }

    var total = 17

    private val mult = 37

    fun append(value: Any?): HashCodeBuilder {
        if (value == null)
            total *= mult
        else {
            if (value.javaClass.isArray) {
                @Suppress("UNCHECKED_CAST")
                append(value as Array<Any?>)
            } else
                total = total * mult + value.hashCode()
        }
        return this
    }

    fun append(array: Array<Any?>): HashCodeBuilder {
        array.forEach {
            total = total * mult + it.hashCode()
        }
        return this
    }

    override fun hashCode(): Int = total

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as HashCodeBuilder
        if (total != other.total) return false
        return true
    }
}