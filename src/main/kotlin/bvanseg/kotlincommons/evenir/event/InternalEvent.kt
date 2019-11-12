/*
 * MIT License
 *
 * Copyright (c) 2019 Boston Vanseghi
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
package bvanseg.kotlincommons.evenir.event

import kotlin.reflect.KFunction
import kotlin.reflect.full.*

/**
 * A wrapper around an event method that was marked by the subscribe event annotation. Does the actual firing of the
 * event method.
 *
 * @author Boston Vanseghi
 * @since 2.1.0
 */
class InternalEvent(
    val function: KFunction<*>,
    private val listener: Any
) {

    fun invoke(e: Any): Any? {
        val pArgs: HashMap<String, Any?> = HashMap()
        val params = function.valueParameters
        val parameter = params[0]
        pArgs[parameter.name!!] = e
        return callNamed(pArgs)
    }

    private fun callNamed(params: Map<String, Any?>, self: Any? = null, extSelf: Any? = null): Any? {
        val map = function.parameters
            .filter { params.containsKey(it.name) }
            .associateWithTo(HashMap()) { params[it.name] }

        map[function.instanceParameter!!] = listener

        self?.let { map[function.instanceParameter!!] = it }
        extSelf?.let { map[function.extensionReceiverParameter!!] = it }
        return function.callBy(map)
    }
}