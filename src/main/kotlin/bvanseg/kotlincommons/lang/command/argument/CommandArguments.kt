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
package bvanseg.kotlincommons.lang.command.argument

import bvanseg.kotlincommons.grouping.collection.linkedListOf
import bvanseg.kotlincommons.lang.command.CommandDispatcher
import bvanseg.kotlincommons.lang.command.dsl.DSLCommand
import bvanseg.kotlincommons.lang.command.dsl.node.DSLCommandNode
import bvanseg.kotlincommons.lang.command.exception.IllegalTokenTypeException
import bvanseg.kotlincommons.lang.command.exception.MissingArgumentException
import bvanseg.kotlincommons.lang.command.token.Token
import bvanseg.kotlincommons.lang.command.token.TokenType
import bvanseg.kotlincommons.lang.command.token.buffer.ArgumentTokenBuffer
import bvanseg.kotlincommons.lang.command.token.buffer.FlagTokenBuffer
import bvanseg.kotlincommons.lang.string.ToStringBuilder
import java.util.LinkedList
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
class CommandArguments(private val dispatcher: CommandDispatcher, private val command: DSLCommand<out Any>) {

    private val arguments: LinkedList<CommandArgument<*>> = linkedListOf()
    private val flags: LinkedList<CommandFlag> = linkedListOf()

    fun hasArgument(): Boolean = arguments.isNotEmpty()
    fun hasNoArguments(): Boolean = arguments.isEmpty()
    fun nextArgument(): CommandArgument<*> = arguments.removeFirst()

    fun hasFlag(): Boolean = flags.isNotEmpty()
    fun hasNoFlags(): Boolean = flags.isEmpty()
    fun nextFlag(): CommandFlag = flags.removeFirst()

    var current: DSLCommandNode = command

    /**
     * Parses the list of [Token]s returned by the [TokenParser].
     *
     * @param tokens The list of tokens to parse as more defined types.
     */
    fun parse(tokens: List<Token>) {
        val argumentTokenBuffer = ArgumentTokenBuffer(tokens)
        val flagTokenBuffer = FlagTokenBuffer(tokens)

        while (argumentTokenBuffer.isNotEmpty()) {
            parseArgument(argumentTokenBuffer)
        }
        while (flagTokenBuffer.isNotEmpty()) {
            parseFlag(flagTokenBuffer.next())
        }

        current = command // Reset the position for a re-parse if necessary.
    }

    private fun parseArgument(tokenBuffer: ArgumentTokenBuffer) {
        val currentArguments = current.arguments

        // We only want the transformers relevant to the current level of arguments.
        val transformersForArguments = currentArguments.filter { it.type != String::class }.map {
            dispatcher.transformers[it.type]!!
        }

        // We store the argument type of the transformer that the raw argument satisfied.
        // This will be used to find the next argument in the chain by its type and set current to that new level.
        lateinit var acceptedType: KClass<*>
        var foundTransformer = false

        // For all potential transformers, test them.
        for (transformer in transformersForArguments) {
            if (transformer.matches(tokenBuffer)) {
                val transformedArgument = transformer.parse(tokenBuffer)
                val commandArgument = CommandArgument(transformedArgument, transformer.type)
                arguments.add(commandArgument)
                acceptedType = transformer.type
                foundTransformer = true
                break
            }
        }

        current = if (!foundTransformer) {
            acceptedType = String::class
            val token = tokenBuffer.next()
            arguments.add(CommandArgument(token.value, String::class))

            current.literals.find { it.literalValue == token.value }
                ?: current.arguments.find { it.type == acceptedType }
                ?: throw MissingArgumentException("Could not find suitable argument or literal for token value '${token.value}'!")
        } else {
            current.arguments.find { it.type == acceptedType || acceptedType.isSubclassOf(it.type) }
                ?: throw MissingArgumentException("Could not find suitable argument for type '$acceptedType'!")
        }
    }

    private fun parseFlag(token: Token) {
        when (token.tokenType) {
            TokenType.SHORT_FLAG -> token.value.substringAfter("-").forEach {
                flags.add(CommandFlag(it.toString()))
            }
            TokenType.LONG_FLAG -> flags.add(CommandFlag(token.value.substringAfter("--")))
            else -> throw IllegalTokenTypeException("Expected token type to be a flag type but was actually '${token.tokenType}'")
        }
    }

    override fun toString(): String = ToStringBuilder.builder(this::class)
        .append("arguments", arguments)
        .append("flags", flags)
        .toString()
}