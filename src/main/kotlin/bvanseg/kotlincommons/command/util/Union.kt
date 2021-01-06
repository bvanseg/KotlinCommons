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
package bvanseg.kotlincommons.command.util

import bvanseg.kotlincommons.command.CommandManager
import bvanseg.kotlincommons.command.context.Context
import java.util.Objects
import kotlin.reflect.KClass

/**
 * Same functionality as an {@link Optional}, but can support two generic types, and not just one.
 *
 * @author Boston Vanseghi
 * @since 2.1.0
 */
class Union<A : Any, B : Any>(
    commandManager: CommandManager<*>,
    context: Context,
    value: String,
    typeA: KClass<A>,
    typeB: KClass<B>
) {
    val first: A? = tryParse(commandManager, context, value, typeA)
    val second: B? = tryParse(commandManager, context, value, typeB)

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> tryParse(
        commandManager: CommandManager<*>,
        context: Context,
        value: String,
        type: KClass<T>
    ): T? = when (type) {
        Byte::class -> value.toByteOrNull()?.let { it as T }
        Short::class -> value.toShortOrNull()?.let { it as T }
        Int::class -> value.toIntOrNull()?.let { it as T }
        Long::class -> value.toLongOrNull()?.let { it as T }
        Double::class -> value.toDoubleOrNull()?.let { it as T }
        Float::class -> value.toFloatOrNull()?.let { it as T }
        Boolean::class -> value.toBoolean() as T
        Char::class -> value.singleOrNull()?.let { it as T }
        String::class -> value as T
        else -> {
            commandManager.transformers[type]?.let {
                it.parse(value, context) as T
            }
        }
    }

    override fun equals(other: Any?): Boolean = other is Union<*, *> && other.first == first && other.second == second

    override fun hashCode(): Int = Objects.hash(first, second)
}