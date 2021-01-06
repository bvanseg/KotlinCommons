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
package bvanseg.kotlincommons.string

/**
 * A class that builds [Any] object into a [String] through appending members. Should be used in toString building.
 *
 * @author Boston Vanseghi
 * @since 2.0.1
 */
class ToStringBuilder private constructor() {

    private val data = StringBuilder()

    private var elements: Int = 0

    fun append(string: String): ToStringBuilder = this.apply { data.append(string) }

    @Suppress("IMPLICIT_CAST_TO_ANY")
    fun <T : Any> append(field: String, value: T): ToStringBuilder = this.apply {
        val con = when (value::class) {
            String::class -> "\"${value as String}\""
            Map::class -> (value as Map<*, *>).entries.joinToString(", ")
            else -> {
                when {
                    value::class.java.isEnum -> value::class.java.enumConstants[(value as Enum<*>).ordinal]
                    value::class.java.isArray -> "[${(value as Array<*>).joinToString(", ")}]"
                    else -> value.toString()
                }
            }
        }

        if (elements == 0)
            data.append("$field=$con")
        else
            data.append(", $field=$con")

        elements++
    }

    override fun toString(): String = "$data)"

    companion object {
        @JvmStatic
        fun builder(clazz: Any): ToStringBuilder = ToStringBuilder().apply {
            this.append("${clazz::class.simpleName}(")
        }
    }
}