package bvanseg.kotlincommons.lang.command.argument

import bvanseg.kotlincommons.grouping.collection.linkedListOf
import bvanseg.kotlincommons.lang.command.CommandDispatcher
import bvanseg.kotlincommons.lang.command.CommandProperties
import bvanseg.kotlincommons.lang.command.dsl.DSLCommand
import bvanseg.kotlincommons.lang.command.dsl.node.DSLCommandNode
import bvanseg.kotlincommons.lang.command.exception.IllegalTokenTypeException
import bvanseg.kotlincommons.lang.command.exception.MissingArgumentException
import bvanseg.kotlincommons.lang.command.token.buffer.ArgumentTokenBuffer
import bvanseg.kotlincommons.lang.command.token.buffer.FlagTokenBuffer
import bvanseg.kotlincommons.lang.command.token.Token
import bvanseg.kotlincommons.lang.command.token.TokenType
import bvanseg.kotlincommons.lang.string.ToStringBuilder
import java.util.LinkedList
import kotlin.reflect.KClass

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
class CommandArguments(private val dispatcher: CommandDispatcher, private val command: DSLCommand<out CommandProperties>) {

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

        while (argumentTokenBuffer.isNotEmpty()) { parseArgument(argumentTokenBuffer) }
        while (flagTokenBuffer.isNotEmpty()) { parseFlag(flagTokenBuffer.next()) }

        current = command // Reset the position for a re-parse if necessary.
    }

    private fun parseArgument(tokenBuffer: ArgumentTokenBuffer) {
        val currentArguments = current.arguments

        // We only want the transformers relevant to the current level of arguments.
        val acceptedArgumentTypes = currentArguments.filter { it.type != String::class }.map { it.type }
        val transformersForArguments = dispatcher.transformers.filter { (type, _) ->
            acceptedArgumentTypes.contains(type)
        }

        // We store the argument type of the transformer that the raw argument satisfied.
        // This will be used to find the next argument in the chain by its type and set current to that new level.
        lateinit var acceptedType: KClass<*>
        var foundTransformer = false

        // For all potential transformers, test them.
        for ((type, transformer) in transformersForArguments) {
            if (transformer.matches(tokenBuffer)) {
                val transformedArgument = transformer.parse(tokenBuffer)
                val commandArgument = CommandArgument(transformedArgument, type)
                arguments.add(commandArgument)
                acceptedType = type
                foundTransformer = true
                break
            }
        }

        if (!foundTransformer) {
            acceptedType = String::class
            val token = tokenBuffer.next()
            arguments.add(CommandArgument(token.value, String::class))

            val literal = current.literals.find { it.literalValue == token.value }

            if (literal != null) {
                current = literal
            } else {
                // If a suitable literal was unable to be found using the accepted type, then attempt to find the next level
                // in the current set of arguments by the argument type.
                val potentialNextNode = current.arguments.find { it.type == acceptedType }

                if (potentialNextNode != null) {
                    current = potentialNextNode
                } else {
                    throw MissingArgumentException("Could not find suitable argument or literal for token value '${token.value}'!")
                }
            }
        } else {
            // If a suitable literal was unable to be found using the accepted type, then attempt to find the next level
            // in the current set of arguments by the argument type.
            val potentialNextNode = current.arguments.find { it.type == acceptedType }

            if (potentialNextNode != null) {
                current = potentialNextNode
            } else {
                throw MissingArgumentException("Could not find suitable argument for type '$acceptedType'!")
            }
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