package bvanseg.kotlincommons.lang.command.dsl

import bvanseg.kotlincommons.lang.command.CommandDispatcher
import bvanseg.kotlincommons.lang.command.argument.CommandArguments
import bvanseg.kotlincommons.lang.command.category.CommandCategory
import bvanseg.kotlincommons.lang.command.context.CommandContext
import bvanseg.kotlincommons.lang.command.dsl.key.DSLFlagKey
import bvanseg.kotlincommons.lang.command.dsl.node.DSLCommandExceptionCatcher
import bvanseg.kotlincommons.lang.command.dsl.node.DSLCommandExecutor
import bvanseg.kotlincommons.lang.command.dsl.node.DSLCommandNode
import bvanseg.kotlincommons.lang.command.exception.DuplicateCatcherException
import bvanseg.kotlincommons.lang.command.exception.MissingArgumentException
import bvanseg.kotlincommons.lang.command.exception.MissingExecutorException
import bvanseg.kotlincommons.lang.command.validator.ValidationResult
import bvanseg.kotlincommons.lang.command.validator.Validator

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
class DSLCommand<T: Any>(val name: String, val aliases: List<String> = listOf()): DSLCommandNode() {

    var exceptionCatcher: DSLCommandExceptionCatcher<*>? = null

    var category: CommandCategory = CommandDispatcher.ROOT_CATEGORY

    lateinit var properties: T

    val usages: MutableList<String> = mutableListOf()
    val examples: MutableList<String> = mutableListOf()

    fun createFlagKey(name: String, vararg names: String): DSLFlagKey = DSLFlagKey(name, names.toList())

    fun run(arguments: CommandArguments, context: CommandContext): Any? {
        var currentLevel: DSLCommandNode = this

        while (arguments.hasArgument()) {
            val commandArg = arguments.nextArgument()
            val commandArgString = commandArg.value.toString()

            // LITERAL HANDLING

            val literal = currentLevel.literals.find { it.literalValue.equals(commandArgString, true) }

            if (literal != null) {
                currentLevel = literal
                context.setArgument(literal.literalValue, literal.literalValue)
                continue
            }

            // ARGUMENT HANDLING

            val argument = currentLevel.arguments.find { it.type == commandArg.type }

            if (argument != null) {
                currentLevel = argument

                if (argument.type == commandArg.type) {
                    argument.validators.forEach {
                        val result = (it as Validator<Any>).validate(commandArg.value)

                        if (result == ValidationResult.INVALID) {
                            return it
                        }
                    }
                }

                if (argument.type == String::class) {
                    context.setArgument(argument.name, commandArgString)
                } else {
                    context.setArgument(argument.name, commandArg.value)
                }
                continue
            } else {
                throw MissingArgumentException("Could not find valid argument type for input '$commandArgString' (type ${commandArg.type})")
            }
        }

        // FLAG HANDLING
        while (arguments.hasFlag()) {
            val commandFlag = arguments.nextFlag()
            context.addFlag(commandFlag.rawValue)
        }

        // EMPTY/FINISHED ARGUMENTS HANDLING
        val executor = currentLevel.executor
        return if (executor != null) {
            try {
                executor.block.invoke(context)
            } catch (e: Exception) {
                this.exceptionCatcher?.block?.invoke(context, e)
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
    fun command(name: String, vararg aliases: String, commandCallback: DSLCommand<T>.() -> Unit): DSLCommand<T> = error("...")
}