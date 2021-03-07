package bvanseg.kotlincommons.lang.command.argument

import bvanseg.kotlincommons.grouping.collection.linkedListOf
import bvanseg.kotlincommons.lang.command.CommandDispatcher
import java.util.LinkedList

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
class CommandArguments(private val dispatcher: CommandDispatcher) {

    private val arguments: LinkedList<CommandArgument<*>> = linkedListOf()

    fun isEmpty(): Boolean = arguments.isEmpty()
    fun isNotEmpty(): Boolean = arguments.isNotEmpty()

    fun nextArgument(): CommandArgument<*> = arguments.removeFirst()

    fun parse(rawArguments: List<String>) {
        rawArguments.forEach { rawArgument ->

            var foundTransformer = false

            for ((type, transformer) in dispatcher.transformers) {
                if (transformer.matches(rawArgument)) {
                    val transformedArgument = transformer.parse(rawArgument)
                    val commandArgument = CommandArgument(transformedArgument, rawArgument, type)
                    arguments.add(commandArgument)
                    foundTransformer = true
                }
            }

            if (!foundTransformer) {
                arguments.add(CommandArgument(rawArgument, rawArgument, String::class))
            }
        }
    }
}