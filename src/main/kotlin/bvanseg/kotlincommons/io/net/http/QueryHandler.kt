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
package bvanseg.kotlincommons.io.net.http

import bvanseg.kotlincommons.grouping.collection.joinToString

/**
 * @author Boston Vanseghi
 * @since 2.5.1
 */
object QueryHandler {

    fun build(map: Map<String, Any?>) =
        "?" + map.filter { it.value != null }.joinToString("&") { pair -> "${pair.key}=${pair.value}" }

    fun build(vararg pairs: Pair<String, Any?>) =
        "?" + pairs.filter { it.second != null }.joinToString("&") { pair -> "${pair.first}=${pair.second}" }

    fun derive(query: String): List<Pair<String, String>> = query.replace("?", "").split('&').run {
        val pairs = mutableListOf<Pair<String, String>>()

        for (param in this) {
            val split = param.split("=")
            pairs.add(split[0] to split[1])
        }

        return pairs
    }
}