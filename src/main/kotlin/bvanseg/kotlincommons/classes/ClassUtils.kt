/*
 * MIT License
 *
 * Copyright (c) 2020 Boston Vanseghi
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
package bvanseg.kotlincommons.classes

import java.lang.reflect.Constructor

fun <T> createNewInstance(clazz: Class<T>): T? = createNewInstance(clazz, null)

fun <T> createNewInstance(clazz: Class<T>, parameterTypes: Array<Class<*>>?, vararg arguments: Any): T {
    lateinit var constructor: Constructor<T>
    try {
        constructor = if (parameterTypes == null)
            clazz.getDeclaredConstructor()
        else
            clazz.getDeclaredConstructor(*parameterTypes)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return constructor.newInstance(*arguments)
}

/**
 * Returns an enum entry with the specified name or `null` if no such entry was found.
 */
inline fun <reified T : Enum<T>> enumValueOfOrNull(name: String): T? = enumValues<T>().find { it.name == name }
