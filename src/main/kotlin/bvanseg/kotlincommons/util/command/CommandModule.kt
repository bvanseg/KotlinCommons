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
package bvanseg.kotlincommons.util.command

import bvanseg.kotlincommons.reflect.getKClass
import bvanseg.kotlincommons.util.command.context.Context
import kotlin.reflect.full.isSubclassOf

/**
 * Used to store commands of the same name, but with different parameter counts/types.
 *
 * Given user input, a command module will automatically sift through recorded command structures and see which one best
 * fits the user's input.
 *
 * @author Boston Vanseghi
 * @since 2.1.0
 */
class CommandModule(val tag: String, val manager: CommandManager<*>) {

    val commands: MutableList<InternalCommand> = mutableListOf()

    /**
     * Finds the most appropriate command to use in handling a set of arguments. Returns null if no such command can be found.
     */
    fun findCandidateCommand(args: String, context: Context): InternalCommand? {
        var dist = Int.MIN_VALUE
        var candidateCommand: InternalCommand? = null
        commands.forEach {
            val firstParam = it.function.parameters.firstOrNull()
            val hasContext = firstParam?.type?.getKClass()?.isSubclassOf(Context::class) ?: false
            val addon = if (hasContext) 1 else 0
            val score2 = it.softInvoke(args, context) - addon
            if (score2 > dist) {
                dist = score2
                candidateCommand = it
            }
        }

        return candidateCommand
    }
}