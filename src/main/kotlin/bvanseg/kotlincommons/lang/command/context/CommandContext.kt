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
package bvanseg.kotlincommons.lang.command.context

import bvanseg.kotlincommons.lang.command.CommandDispatcher
import bvanseg.kotlincommons.lang.command.dsl.key.DSLFlagKey
import bvanseg.kotlincommons.lang.command.dsl.key.DSLKey
import bvanseg.kotlincommons.lang.command.token.Token

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
open class CommandContext(val dispatcher: CommandDispatcher) {

    lateinit var rawInput: String
    lateinit var splitRawInput: List<String>
    lateinit var tokenizedArguments: List<Token>

    private val argumentMap: HashMap<String, Any> = hashMapOf()
    private val flagSet: HashSet<String> = hashSetOf()

    fun <T : Any> getArgument(key: DSLKey<T>): T = getArgument(key.name)
    fun <T : Any> getArgument(name: String): T = argumentMap[name] as T
    fun setArgument(name: String, value: Any) = argumentMap.putIfAbsent(name, value)

    fun hasFlag(key: DSLFlagKey): Boolean = hasFlag(key.name, *key.names.toTypedArray())
    fun hasFlag(name: String, vararg names: String): Boolean {
        if (flagSet.contains(name)) return true
        for (n in names) {
            if (flagSet.contains(n)) {
                return true
            }
        }
        return false
    }

    fun addFlag(name: String) = flagSet.add(name)
}