package bvanseg.kotlincommons.lang.command.dsl

import bvanseg.kotlincommons.lang.command.CommandProperties
import bvanseg.kotlincommons.lang.command.argument.CommandArguments
import bvanseg.kotlincommons.lang.command.category.SimpleCategoryNode
import bvanseg.kotlincommons.lang.command.context.CommandContext
import bvanseg.kotlincommons.lang.command.validator.Validator

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
class DSLCommand<T: CommandProperties>(val name: String, val aliases: List<String> = listOf()): DSLCommandNode() {

    var categories: SimpleCategoryNode? = null
        private set

    lateinit var properties: T

    val usages: MutableList<String> = mutableListOf()
    val examples: MutableList<String> = mutableListOf()

    fun createFlagKey(name: String, vararg names: String): DSLFlagKey = DSLFlagKey(name, names.toList())

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

        while (arguments.hasArgument()) {
            val commandArg = arguments.nextArgument()

            // LITERAL HANDLING

            val literal = currentLevel.literals.find { it.literalValue.equals(commandArg.rawValue, true) }

            if (literal != null) {
                currentLevel = literal
                context.setArgument(literal.literalValue, literal.literalValue)
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
                    context.setArgument(argument.name, commandArg.value.toString())
                } else {
                    context.setArgument(argument.name, commandArg.value)
                }
                continue
            }
        }

        // FLAG HANDLING
        while (arguments.hasFlag()) {
            val commandFlag = arguments.nextFlag()
            context.addFlag(commandFlag.rawValue)
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
    fun command(name: String, vararg aliases: String, commandCallback: DSLCommand<T>.() -> Unit): DSLCommand<T> = error("...")
}