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
package bvanseg.kotlincommons.type

import bvanseg.kotlincommons.project.Experimental
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Inspired by the fasterXML implementation of this class.
 *
 * @author fasterXML Developers
 * @author Boston Vanseghi
 * @since 2.7.0
 */
@Experimental
class TypeReference<T> : Comparable<TypeReference<T>> {
    var type: Type

    override fun compareTo(other: TypeReference<T>): Int = 0

    init {
        val superClass = this.javaClass.genericSuperclass
        require(superClass !is Class<*>) { "Internal error: TypeReference constructed without actual type information" }
        type = (superClass as ParameterizedType).actualTypeArguments[0]
    }
}