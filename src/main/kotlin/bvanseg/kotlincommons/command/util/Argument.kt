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
import kotlin.reflect.KClass

/**
 * Represents a "wildcard" argument, where the type can be anything. Getter methods will return null if the input can't
 * be properly converted.
 *
 * @author Boston Vanseghi
 * @since 2.1.0
 */
class Argument(private val raw: String) {

    fun getRaw(): String = raw

    fun getAsByte(): Byte? = raw.toByteOrNull()
    fun getAsShort(): Short? = raw.toShortOrNull()
    fun getAsInt(): Int? = raw.toIntOrNull()
    fun getAsLong(): Long? = raw.toLongOrNull()

    fun getAsFloat(): Float? = raw.toFloatOrNull()
    fun getAsDouble(): Double? = raw.toDoubleOrNull()

    /**
     * Transforms raw input into any registered {@link Transformer} type from within the {@link Command} function. Will
     * return null if the transformation fails.
     */
    fun <T : Any> transformTo(commandManager: CommandManager<*>, clazz: KClass<T>): Any? =
        commandManager.transformers[clazz]?.parse(raw)
}