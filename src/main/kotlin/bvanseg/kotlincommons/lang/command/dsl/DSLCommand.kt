package bvanseg.kotlincommons.lang.command.dsl

import bvanseg.kotlincommons.lang.command.argument.CommandArguments
import bvanseg.kotlincommons.lang.command.category.SimpleCategoryNode
import bvanseg.kotlincommons.lang.command.context.CommandContext
import bvanseg.kotlincommons.lang.command.validator.Validator

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
class DSLCommand(val name: String, val aliases: List<String> = listOf()): DSLCommandNode() {
    var description: String = ""

    var categories: SimpleCategoryNode? = null
        private set

    fun addToCategory(category: String, vararg subcategories: String) {
        val rootCategory = SimpleCategoryNode(category.toLowerCase())
        categories = rootCategory

        var current = rootCategory

        for (subcategory in subcategories) {
            val newCategoryNode = SimpleCategoryNode(subcategory)
            current.next = newCategoryNode
            current = newCategoryNode
        }
    }

    fun run(arguments: CommandArguments, context: CommandContext): Any? {
        var currentLevel: DSLCommandNode = this

        while (arguments.isNotEmpty()) {
            val commandArg = arguments.nextArgument()

            // LITERAL HANDLING

            val literal = currentLevel.literals.find { it.literalValue.equals(commandArg.rawValue, true) }

            if (literal != null) {
                currentLevel = literal
                context.set(literal.literalValue, literal.literalValue)
                continue
            }

            // ARGUMENT HANDLING

            val argument = currentLevel.arguments.find { it.type == commandArg.type || it.type == String::class }

            if (argument != null) {
                currentLevel = argument

                if (argument.type == commandArg.type &&
                    argument.validators.any { !(it as Validator<Any>).validate(commandArg.value) }) {
                    // TODO: Do something meaningful here.
                    return null
                } else if (argument.type == String::class &&
                    argument.validators.any { !(it as Validator<String>).validate(commandArg.value.toString()) }) {
                    // TODO: Do something meaningful here.
                    return null
                }

                if (argument.type == String::class) {
                    context.set(argument.name, commandArg.value.toString())
                } else {
                    context.set(argument.name, commandArg.value)
                }
                continue
            }
        }

        // EMPTY/FINISHED ARGUMENTS HANDLING
        val executor = currentLevel.executor
        if (executor != null) {
            return executor.block.invoke(context)
        } else {
            throw RuntimeException("Expected execute block but could not find any!")
        }
    }

    @Deprecated(
        "DSL command blocks can not be nested!", level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("error(\"...\")")
    )
    fun command(name: String, vararg aliases: String, commandCallback: DSLCommand.() -> Unit): DSLCommand = error("...")
}