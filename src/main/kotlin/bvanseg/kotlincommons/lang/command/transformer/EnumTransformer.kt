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

import bvanseg.kotlincommons.grouping.enum.enumValueOfOrNull
import bvanseg.kotlincommons.grouping.enum.getOrNull
import bvanseg.kotlincommons.lang.command.context.CommandContext
import bvanseg.kotlincommons.lang.command.token.buffer.ArgumentTokenBuffer
import bvanseg.kotlincommons.lang.command.token.buffer.PeekingTokenBuffer
import kotlin.reflect.KClass

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
abstract class EnumTransformer<E : Enum<E>>(type: KClass<E>) : Transformer<E>(type) {
    override fun matches(buffer: PeekingTokenBuffer, context: CommandContext): Boolean {
        val text = buffer.peek()?.value ?: return false
        text.toIntOrNull()?.let { type.getOrNull(it)?.let { return true } }
        return type.enumValueOfOrNull(text, true) != null
    }

    override fun parse(buffer: ArgumentTokenBuffer, context: CommandContext): E = buffer.next().value.let { input ->
        input.toIntOrNull()?.let { type.getOrNull(it) } ?: type.enumValueOfOrNull(input, true)!!
    }
}