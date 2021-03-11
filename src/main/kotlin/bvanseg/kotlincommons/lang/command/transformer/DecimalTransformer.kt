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
package bvanseg.kotlincommons.lang.command.transformer

import bvanseg.kotlincommons.lang.command.exception.TransformerException
import bvanseg.kotlincommons.lang.command.token.buffer.ArgumentTokenBuffer
import bvanseg.kotlincommons.lang.command.token.buffer.PeekingTokenBuffer
import java.math.BigDecimal
import kotlin.reflect.KClass

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
abstract class DecimalTransformer internal constructor(type: KClass<out Number>) : Transformer<Number>(type) {
    private val REGEX = Regex("^[+-]?([0-9]*[.])?[0-9]+$")
    private val MIN =
        if (type == Float::class) BigDecimal.valueOf(Float.MIN_VALUE.toDouble()) else BigDecimal.valueOf(Double.MIN_VALUE)
    private val MAX =
        if (type == Float::class) BigDecimal.valueOf(Float.MAX_VALUE.toDouble()) else BigDecimal.valueOf(Double.MAX_VALUE)

    override fun matches(buffer: PeekingTokenBuffer): Boolean {
        val token = buffer.peek() ?: return false
        val input = token.value
        return input.matches(REGEX) || input.equals("pi", true) || input.equals("e", true)
    }

    override fun parse(buffer: ArgumentTokenBuffer): Number {
        val text = buffer.next().value.replace("pi", Math.PI.toString(), true)
            .replace("e", Math.E.toString(), true)
        val bigDeci = BigDecimal(text)
        if (bigDeci !in MIN..MAX) {
            throw TransformerException("Value '$bigDeci' does not fall within the range of ($MIN to $MAX)")
        }
        return if (type == Float::class) text.toFloat() else text.toDouble()
    }
}