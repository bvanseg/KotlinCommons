package bvanseg.kotlincommons.lang.command

import bvanseg.kotlincommons.lang.command.argument.CommandArguments
import bvanseg.kotlincommons.lang.command.category.CommandCategory
import bvanseg.kotlincommons.lang.command.context.CommandContext
import bvanseg.kotlincommons.lang.command.context.DefaultCommandContext
import bvanseg.kotlincommons.lang.command.dsl.DSLCommand
import bvanseg.kotlincommons.lang.command.token.TokenParser
import bvanseg.kotlincommons.lang.command.transformer.Transformer
import bvanseg.kotlincommons.lang.command.transformer.impl.BigDecimalTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.BigIntegerTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.BooleanTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.ByteTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.CharTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.ChronoUnitTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.ColorTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.DoubleTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.FloatTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.IntTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.KhronoUnitTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.LongTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.ShortTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.TimeUnitTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.ArgumentTokenBufferTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.InstantTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.LocalDateTimeTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.LocalDateTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.LocalTimeTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.OffsetDateTimeTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.TokenTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.UUIDTransformer
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import kotlin.reflect.KClass

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
class CommandDispatcher(private val prefix: String) {

    companion object {
        val ROOT_CATEGORY = CommandCategory("Root", "*")
    }

    private val commands: ConcurrentMap<String, DSLCommand<out CommandProperties>> = ConcurrentHashMap()
    private val categories: ConcurrentMap<CommandCategory, MutableList<DSLCommand<out CommandProperties>>> = ConcurrentHashMap()

    val transformers: ConcurrentMap<KClass<*>, Transformer<*>> = ConcurrentHashMap()

    init {
        registerTransformer(CharTransformer)
        registerTransformer(BooleanTransformer)

        registerTransformer(ByteTransformer)
        registerTransformer(ShortTransformer)
        registerTransformer(IntTransformer)
        registerTransformer(LongTransformer)

        registerTransformer(FloatTransformer)
        registerTransformer(DoubleTransformer)

        registerTransformer(ArgumentTokenBufferTransformer)
        registerTransformer(BigDecimalTransformer)
        registerTransformer(BigIntegerTransformer)
        registerTransformer(ColorTransformer)
        registerTransformer(InstantTransformer)
        registerTransformer(LocalDateTimeTransformer)
        registerTransformer(LocalDateTransformer)
        registerTransformer(LocalTimeTransformer)
        registerTransformer(OffsetDateTimeTransformer)
        registerTransformer(TokenTransformer)
        registerTransformer(UUIDTransformer)

        registerTransformer(ChronoUnitTransformer)
        registerTransformer(KhronoUnitTransformer)
        registerTransformer(TimeUnitTransformer)
    }

    fun execute(input: String, commandContext: CommandContext = DefaultCommandContext(this)) = execute(prefix, input, commandContext)

    fun execute(prefix: String, input: String, commandContext: CommandContext = DefaultCommandContext(this)): Any? {
        if (input.isBlank()) return null
        if (!input.startsWith(prefix)) return null
        val trimmedInput = input.trim()

        val parts = trimmedInput.split(" ", limit = 2)
        val commandReference = parts[0]
        val commandName = commandReference.substring(prefix.length, commandReference.length)

        val command = commands[commandName] ?: return null

        val hasArguments = parts.size > 1

        commandContext.rawInput = input
        commandContext.splitRawInput = parts

        if (hasArguments) {
            val arguments = parts[1]
            val tokenParser = TokenParser(arguments)
            commandContext.tokenizedArguments = tokenParser.getAllTokens()
        } else {
            commandContext.tokenizedArguments = emptyList()
        }

        val commandArguments = CommandArguments(this, command)
        commandArguments.parse(commandContext.tokenizedArguments)

        return command.run(commandArguments, commandContext)
    }

    fun getCommandByName(name: String): DSLCommand<out CommandProperties>? = commands[name]
    fun getCommandsByCategoryPath(path: String): List<DSLCommand<out CommandProperties>> = categories[CommandCategory("", path)] ?: emptyList()

    fun getCategories(): Map<CommandCategory, List<DSLCommand<out CommandProperties>>> = categories
    fun getRootCommands(): List<DSLCommand<out CommandProperties>> = categories[ROOT_CATEGORY] ?: emptyList()

    fun registerCommand(command: DSLCommand<out CommandProperties>) {
        commands.putIfAbsent(command.name, command)
        command.aliases.forEach { alias ->
            commands.putIfAbsent(alias, command)
        }

        categories.computeIfAbsent(command.category) { mutableListOf() }.add(command)
    }

    fun registerTransformer(transformer: Transformer<*>) = transformers.putIfAbsent(transformer.type, transformer)
}