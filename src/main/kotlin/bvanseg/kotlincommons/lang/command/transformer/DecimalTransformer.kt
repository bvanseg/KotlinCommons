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

import bvanseg.kotlincommons.lang.command.context.CommandContext
import bvanseg.kotlincommons.lang.command.exception.TransformerParseException
import bvanseg.kotlincommons.lang.command.token.buffer.ArgumentTokenBuffer
import bvanseg.kotlincommons.lang.command.token.buffer.PeekingTokenBuffer
import java.math.BigDecimal
import kotlin.reflect.KClass

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
abstract class DecimalTransformer<T : Number>(
    type: KClass<T>,
    min: BigDecimal,
    max: BigDecimal,
    private val stringToNum: (String) -> T
) : Transformer<T>(type) {
    companion object {
        val REGEX = Regex("^[+-]?([0-9]*[.])?[0-9]+$")
    }

    private val range: ClosedRange<BigDecimal> = min..max

    override fun matches(buffer: PeekingTokenBuffer, context: CommandContext): Boolean =
        buffer.peek()?.value?.let { it.equals("pi", true) || it.equals("e", true) || it.matches(REGEX) } ?: false

    override fun parse(buffer: ArgumentTokenBuffer, context: CommandContext): T {
        var text = buffer.next().value
        if (text.equals("pi", true)) {
            text = Math.PI.toString()
        } else if (text.equals("e", true)) {
            text = Math.E.toString()
        }
        val bigDeci = BigDecimal(text)
        if (bigDeci !in range) {
            throw TransformerParseException("Value '$bigDeci' does not fall within the range of (${range.start} to ${range.endInclusive})")
        }
        return stringToNum(text)
    }
}
