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
package bvanseg.kotlincommons.lang.command.dsl.node

import bvanseg.kotlincommons.lang.check.Check
import bvanseg.kotlincommons.lang.check.Checks
import bvanseg.kotlincommons.lang.command.context.CommandContext
import bvanseg.kotlincommons.lang.command.dsl.key.DSLArgumentKey
import bvanseg.kotlincommons.lang.command.dsl.key.DSLLiteralKey
import bvanseg.kotlincommons.lang.command.exception.DuplicateArgumentTypeException
import bvanseg.kotlincommons.lang.command.exception.DuplicateExecutorException
import bvanseg.kotlincommons.lang.command.exception.DuplicateLiteralException

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
abstract class DSLCommandNode {
    var description: String = ""

    val literals: MutableList<DSLCommandLiteral> = mutableListOf()
    val arguments: MutableList<DSLCommandArgument<*>> = mutableListOf()
    var executor: DSLCommandExecutor<*>? = null

    private fun validateLiteralValue(literalValue: String) {
        Check.all(literalValue, "literal", Checks.notBlank, Checks.noWhitespace)

        literals.forEach { dslLiteral ->
            if (literalValue.equals(dslLiteral.literalValue, true)) {
                throw DuplicateLiteralException("Duplicate literal: '$literalValue'")
            }
        }
    }

    private fun processLiteral(literalValue: String, block: DSLCommandLiteral.(DSLLiteralKey) -> Unit) {
        val spliterals = literalValue.split(" ")

        // If multiple literals, handle as chain. Otherwise, handle as just one literal.
        if (spliterals.size > 1) {
            var currentLiterals: MutableList<DSLCommandLiteral> = literals
            for (i in 0 until spliterals.size - 1) {
                val soloLiteral = spliterals[i]
                validateLiteralValue(soloLiteral)
                val soloLiteralNode = DSLCommandLiteral(this, soloLiteral)
                currentLiterals.add(soloLiteralNode)
                currentLiterals = soloLiteralNode.literals
            }
            // The last node in the chain is special because the last node gets the block applied to it
            val lastLiteral = spliterals.last()
            validateLiteralValue(lastLiteral)
            val lastLiteralNode = DSLCommandLiteral(this, lastLiteral)
            block.invoke(lastLiteralNode, DSLLiteralKey(lastLiteralNode, lastLiteral)) // block applied here
            currentLiterals.add(lastLiteralNode)
        } else {
            val soloLiteral = spliterals.first()
            validateLiteralValue(soloLiteral)
            val soloLiteralNode = DSLCommandLiteral(this, soloLiteral)
            block.invoke(soloLiteralNode, DSLLiteralKey(soloLiteralNode, soloLiteral))
            literals.add(soloLiteralNode)
        }
    }

    fun literal(
        literalValue: String,
        vararg extraLiteralValues: String,
        block: DSLCommandLiteral.(DSLLiteralKey) -> Unit
    ) {
        processLiteral(literalValue, block)
        for (extraLiteralValue in extraLiteralValues) {
            processLiteral(extraLiteralValue, block)
        }
    }

    inline fun <reified T : Any> argument(
        name: String,
        block: DSLCommandArgument<T>.(DSLArgumentKey<T>) -> Unit
    ): DSLCommandArgument<T> {
        Check.all(name, "argument", Checks.notBlank, Checks.noWhitespace)

        val argumentType = T::class

        arguments.forEach {
            if (it.type == argumentType) {
                throw DuplicateArgumentTypeException("Duplicate argument type in same level: $argumentType")
            }
        }

        val argument = DSLCommandArgument(this, name, argumentType)
        block.invoke(argument, DSLArgumentKey(argument, name, argumentType))
        arguments.add(argument)
        return argument
    }

    inline fun <reified T : Any, reified U : Any> arguments(
        arg1Name: String,
        arg2Name: String,
        block: DSLCommandArgument<U>.(DSLArgumentKey<T>, DSLArgumentKey<U>) -> Unit
    ): DSLCommandArgument<U> {
        Check.all(arg1Name, "argument1", Checks.notBlank, Checks.noWhitespace)
        Check.all(arg2Name, "argument2", Checks.notBlank, Checks.noWhitespace)

        val argument1Type = T::class
        val argument2Type = U::class

        arguments.forEach {
            if (it.type == argument1Type) {
                throw DuplicateArgumentTypeException("Duplicate argument type in same level: $argument1Type")
            }
            if (it.type == argument2Type) {
                throw DuplicateArgumentTypeException("Duplicate argument type in same level: $argument2Type")
            }
        }

        val argument1 = DSLCommandArgument(this, arg1Name, argument1Type)
        val argument2 = DSLCommandArgument(this, arg2Name, argument2Type)

        block.invoke(
            argument2,
            DSLArgumentKey(argument1, arg1Name, argument1Type),
            DSLArgumentKey(argument2, arg2Name, argument2Type)
        )
        arguments.add(argument1)
        argument1.arguments.add(argument2)

        return argument2
    }

    inline fun <reified T : Any, reified U : Any, reified V : Any> arguments(
        arg1Name: String,
        arg2Name: String,
        arg3Name: String,
        block: DSLCommandArgument<V>.(DSLArgumentKey<T>, DSLArgumentKey<U>, DSLArgumentKey<V>) -> Unit
    ): DSLCommandArgument<V> {
        Check.all(arg1Name, "argument1", Checks.notBlank, Checks.noWhitespace)
        Check.all(arg2Name, "argument2", Checks.notBlank, Checks.noWhitespace)
        Check.all(arg3Name, "argument3", Checks.notBlank, Checks.noWhitespace)

        val argument1Type = T::class
        val argument2Type = U::class
        val argument3Type = V::class

        arguments.forEach {
            if (it.type == argument1Type) {
                throw DuplicateArgumentTypeException("Duplicate argument type in same level: $argument1Type")
            }
            if (it.type == argument2Type) {
                throw DuplicateArgumentTypeException("Duplicate argument type in same level: $argument2Type")
            }
            if (it.type == argument3Type) {
                throw DuplicateArgumentTypeException("Duplicate argument type in same level: $argument3Type")
            }
        }

        val argument1 = DSLCommandArgument(this, arg1Name, argument1Type)
        val argument2 = DSLCommandArgument(this, arg2Name, argument2Type)
        val argument3 = DSLCommandArgument(this, arg3Name, argument3Type)

        block.invoke(
            argument3,
            DSLArgumentKey(argument1, arg1Name, argument1Type),
            DSLArgumentKey(argument2, arg2Name, argument2Type),
            DSLArgumentKey(argument3, arg3Name, argument3Type)
        )
        arguments.add(argument1)
        argument1.arguments.add(argument2)
        argument2.arguments.add(argument3)

        return argument3
    }

    inline fun <reified T : Any, reified U : Any, reified V : Any, reified W : Any> arguments(
        arg1Name: String,
        arg2Name: String,
        arg3Name: String,
        arg4Name: String,
        block: DSLCommandArgument<W>.(DSLArgumentKey<T>, DSLArgumentKey<U>, DSLArgumentKey<V>, DSLArgumentKey<W>) -> Unit
    ): DSLCommandArgument<W> {
        Check.all(arg1Name, "argument1", Checks.notBlank, Checks.noWhitespace)
        Check.all(arg2Name, "argument2", Checks.notBlank, Checks.noWhitespace)
        Check.all(arg3Name, "argument3", Checks.notBlank, Checks.noWhitespace)
        Check.all(arg4Name, "argument4", Checks.notBlank, Checks.noWhitespace)

        val argument1Type = T::class
        val argument2Type = U::class
        val argument3Type = V::class
        val argument4Type = W::class

        arguments.forEach {
            if (it.type == argument1Type) {
                throw DuplicateArgumentTypeException("Duplicate argument type in same level: $argument1Type")
            }
            if (it.type == argument2Type) {
                throw DuplicateArgumentTypeException("Duplicate argument type in same level: $argument2Type")
            }
            if (it.type == argument3Type) {
                throw DuplicateArgumentTypeException("Duplicate argument type in same level: $argument3Type")
            }
            if (it.type == argument4Type) {
                throw DuplicateArgumentTypeException("Duplicate argument type in same level: $argument4Type")
            }
        }

        val argument1 = DSLCommandArgument(this, arg1Name, argument1Type)
        val argument2 = DSLCommandArgument(this, arg2Name, argument2Type)
        val argument3 = DSLCommandArgument(this, arg3Name, argument3Type)
        val argument4 = DSLCommandArgument(this, arg4Name, argument4Type)

        block.invoke(
            argument4,
            DSLArgumentKey(argument1, arg1Name, argument1Type),
            DSLArgumentKey(argument2, arg2Name, argument2Type),
            DSLArgumentKey(argument3, arg3Name, argument3Type),
            DSLArgumentKey(argument4, arg4Name, argument4Type)
        )
        arguments.add(argument1)
        argument1.arguments.add(argument2)
        argument2.arguments.add(argument3)
        argument3.arguments.add(argument4)

        return argument4
    }

    fun <T> executes(block: (CommandContext) -> T): DSLCommandExecutor<T> {
        if (executor != null) {
            throw DuplicateExecutorException("Executor already exists on level!")
        }
        val executor = DSLCommandExecutor(this, block)
        this.executor = executor
        return executor
    }
}