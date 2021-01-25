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
package bvanseg.kotlincommons.util.command.transformer

import bvanseg.kotlincommons.util.command.context.Context
import bvanseg.kotlincommons.util.command.context.EmptyContext
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

/**
 * Turns raw string input into an object of the desired type.
 *
 * @author Boston Vanseghi
 * @since 2.1.0
 */
abstract class Transformer<T : Any> {
    val type: KClass<T>

    constructor(type: KClass<T>) {
        this.type = type
    }

    constructor(type: Class<T>) : this(type.kotlin)

    /**
     * Tries to parse the [input] to the type [T]
     * Returns null if unsuccessful
     */
    abstract fun parse(input: String, ctx: Context? = EmptyContext): T?

    open fun parse(parameter: KParameter, input: String, ctx: Context? = EmptyContext): T? = parse(input, ctx)

    /**
     * Suggests possible values that contain the given [input] argument
     */
    open fun suggest(input: String, ctx: Context? = EmptyContext): List<String> = emptyList()
}