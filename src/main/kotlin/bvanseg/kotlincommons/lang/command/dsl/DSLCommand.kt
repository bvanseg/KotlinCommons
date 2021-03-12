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
package bvanseg.kotlincommons.lang.command.dsl

import bvanseg.kotlincommons.io.logging.debug
import bvanseg.kotlincommons.io.logging.getLogger
import bvanseg.kotlincommons.lang.command.CommandDispatcher
import bvanseg.kotlincommons.lang.command.argument.CommandArguments
import bvanseg.kotlincommons.lang.command.category.CommandCategory
import bvanseg.kotlincommons.lang.command.context.CommandContext
import bvanseg.kotlincommons.lang.command.dsl.key.DSLFlagKey
import bvanseg.kotlincommons.lang.command.dsl.node.DSLCommandExceptionCatcher
import bvanseg.kotlincommons.lang.command.dsl.node.DSLCommandNode
import bvanseg.kotlincommons.lang.command.exception.DuplicateCatcherException
import bvanseg.kotlincommons.lang.command.exception.MissingArgumentException
import bvanseg.kotlincommons.lang.command.exception.MissingExecutorException
import bvanseg.kotlincommons.lang.command.validator.ValidationResult
import bvanseg.kotlincommons.lang.command.validator.Validator
import kotlin.reflect.full.isSubclassOf

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
class DSLCommand<T : Any>(val name: String, val aliases: MutableList<String> = mutableListOf()) : DSLCommandNode() {

    companion object {
        private val logger = getLogger()
    }

    var exceptionCatcher: DSLCommandExceptionCatcher<*>? = null

    var category: CommandCategory = CommandDispatcher.ROOT_CATEGORY

    lateinit var properties: T

    val usages: MutableList<String> = mutableListOf()
    val examples: MutableList<String> = mutableListOf()

    private val flagKeys: MutableList<DSLFlagKey> = mutableListOf()

    fun createFlagKey(name: String, vararg names: String, description: String = ""): DSLFlagKey = DSLFlagKey(name, names.toList(), description).also { flagKeys.add(it) }

    fun getFlagKeys(): List<DSLFlagKey> = flagKeys

    fun run(arguments: CommandArguments, context: CommandContext): Any? {
        var currentLevel: DSLCommandNode = this

        logger.debug { "Preparing to descend into command argument tree with arguments $arguments" }
        while (arguments.hasArgument()) {
            val commandArg = arguments.nextArgument()
            val commandArgString = commandArg.value.toString()

            // LITERAL HANDLING

            val literal = currentLevel.literals.find { it.literalValue.equals(commandArgString, true) }

            if (literal != null) {
                currentLevel = literal
                context.setArgument(literal.literalValue, literal.literalValue)
                logger.debug { "Got literal '${literal.literalValue}!" }
                continue
            }

            // ARGUMENT HANDLING

            val argument = currentLevel.arguments.find { it.type == commandArg.type || commandArg.type.isSubclassOf(it.type)}

            if (argument != null) {
                currentLevel = argument

                if (argument.type == commandArg.type) {
                    argument.validators.forEach {
                        val result = (it as Validator<Any>).validate(commandArg.value)

                        if (result == ValidationResult.INVALID) {
                            logger.debug { "Command '$name' failed on validation step for validator '$it', aborting command execution!" }
                            return it
                        }
                    }
                }

                if (argument.type == String::class) {
                    context.setArgument(argument.name, commandArgString)
                } else {
                    context.setArgument(argument.name, commandArg.value)
                }
                logger.debug { "Got argument '${argument.name}' of type '${argument.type}' with value '$commandArgString'!" }
                continue
            } else {
                throw MissingArgumentException("Could not find valid argument type for input '$commandArgString' (type ${commandArg.type})")
            }
        }

        logger.debug { "Preparing to handle command flags with arguments $arguments" }
        // FLAG HANDLING
        while (arguments.hasFlag()) {
            val commandFlag = arguments.nextFlag()
            context.addFlag(commandFlag.rawValue)
            logger.debug { "Got flag '${commandFlag.rawValue}'!" }
        }

        // EMPTY/FINISHED ARGUMENTS HANDLING
        val executor = currentLevel.executor
        return if (executor != null) {
            if (this.exceptionCatcher != null) {
                try {
                    logger.debug("Finalizing command run, preparing to execute with exception catcher!")
                    executor.block.invoke(context)
                } catch (e: Exception) {
                    logger.error("An exception occurred while running a command. Delegating exception handling to exception catcher...")
                    this.exceptionCatcher!!.block.invoke(context, e)
                }
            } else {
                logger.debug("Finalizing command run, preparing to execute without exception catcher!")
                executor.block.invoke(context)
            }
        } else {
            throw MissingExecutorException("Expected execute block but could not find any!")
        }
    }

    fun <T> catchException(block: (CommandContext, Throwable) -> T): DSLCommandExceptionCatcher<T> {
        if (exceptionCatcher != null) {
            throw DuplicateCatcherException("Exception catcher already exists for this command!")
        }
        val exceptionCatcher = DSLCommandExceptionCatcher(this, block)
        this.exceptionCatcher = exceptionCatcher
        return exceptionCatcher
    }

    @Deprecated(
        "DSL command blocks can not be nested!", level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("error(\"...\")")
    )
    fun command(name: String, vararg aliases: String, commandCallback: DSLCommand<T>.() -> Unit): DSLCommand<T> =
        error("...")
}