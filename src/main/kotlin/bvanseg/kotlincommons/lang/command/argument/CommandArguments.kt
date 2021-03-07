package bvanseg.kotlincommons.lang.command.argument

import bvanseg.kotlincommons.grouping.collection.linkedListOf
import bvanseg.kotlincommons.lang.command.CommandDispatcher
import bvanseg.kotlincommons.lang.command.dsl.DSLCommand
import bvanseg.kotlincommons.lang.command.dsl.DSLCommandNode
import java.util.LinkedList
import kotlin.reflect.KClass

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
class CommandArguments(private val dispatcher: CommandDispatcher, private val command: DSLCommand) {

    private val arguments: LinkedList<CommandArgument<*>> = linkedListOf()

    fun isEmpty(): Boolean = arguments.isEmpty()
    fun isNotEmpty(): Boolean = arguments.isNotEmpty()

    fun nextArgument(): CommandArgument<*> = arguments.removeFirst()

    fun parse(rawArguments: List<String>) {

        var current: DSLCommandNode = command

        rawArguments.forEach { rawArgument ->
            // The current arguments to match the raw argument up against.
            val currentArguments = current.arguments

            // We only want the transformers relevant to the current level of arguments.
            val acceptedArgumentTypes = currentArguments.map { it.type }
            val transformersForArguments = dispatcher.transformers.filter { (type, _) ->
                acceptedArgumentTypes.contains(type)
            }

            // We store the argument type of the transformer that the raw argument satisfied.
            // This will be used to find the next argument in the chain by its type and set current to that new level.
            lateinit var acceptedType: KClass<*>
            var foundTransformer = false

            // For all potential transformers, test them.
            for ((type, transformer) in transformersForArguments) {
                if (transformer.matches(rawArgument)) {
                    val transformedArgument = transformer.parse(rawArgument)
                    val commandArgument = CommandArgument(transformedArgument, rawArgument, type)
                    arguments.add(commandArgument)
                    acceptedType = type
                    foundTransformer = true
                }
            }

            if (!foundTransformer) {
                acceptedType = String::class
                arguments.add(CommandArgument(rawArgument, rawArgument, String::class))
            }

            // We attempt to use the accepted type to move deeper into the tree and on to the next level.
            // Literals take priority, so check literals first.


            val literal = current.literals.find { it.literalValue == rawArgument }

            if (literal != null) {
                current = literal
            } else {
                // If a suitable literal was unable to be found using the accepted type, then attempt to find the next level
                // in the current set of arguments by the argument type.

                val potentialNextNode = current.arguments.find { it.type == acceptedType }

                if (potentialNextNode != null) {
                    current = potentialNextNode
                } else {
                    throw IllegalArgumentException("Could not find suitable argument or literal for raw argument '$rawArgument'!")
                }
            }
        }
    }
}