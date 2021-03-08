package bvanseg.kotlincommons.lang.command

import bvanseg.kotlincommons.lang.command.argument.CommandArguments
import bvanseg.kotlincommons.lang.command.category.CategoryTreeNode
import bvanseg.kotlincommons.lang.command.category.SimpleCategoryNode
import bvanseg.kotlincommons.lang.command.context.CommandContext
import bvanseg.kotlincommons.lang.command.context.DefaultCommandContext
import bvanseg.kotlincommons.lang.command.dsl.DSLCommand
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
import bvanseg.kotlincommons.lang.command.transformer.impl.UUIDTransformer
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import kotlin.reflect.KClass

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
class CommandDispatcher(private val prefix: String) {

    private val commands: ConcurrentMap<String, DSLCommand> = ConcurrentHashMap()
    val categories: CategoryTreeNode = CategoryTreeNode(null, "")

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

        registerTransformer(BigDecimalTransformer)
        registerTransformer(BigIntegerTransformer)
        registerTransformer(ColorTransformer)
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

        val parts = trimmedInput.split(" ")
        val commandReference = parts[0]
        val commandName = commandReference.substring(prefix.length, commandReference.length)
        val arguments = parts.subList(1, parts.size)

        val command = commands[commandName] ?: return null

        // Initialize command context
        commandContext.rawInput = input
        commandContext.splitRawInput = parts
        commandContext.splitArguments = arguments

        val commandArguments = CommandArguments(this, command)
        commandArguments.parse(arguments)

        return command.run(commandArguments, commandContext)
    }

    fun registerCommand(command: DSLCommand) {
        commands.putIfAbsent(command.name, command)
        command.aliases.forEach { alias ->
            commands.putIfAbsent(alias, command)
        }

        var currentDispatcherCategoryNode: CategoryTreeNode? = this.categories
        var currentCommandCategoryNode: SimpleCategoryNode? = command.categories

        if (currentCommandCategoryNode != null) {
            this.categories.populateChain(currentCommandCategoryNode)

            // Add to root.
            currentDispatcherCategoryNode!!.commands.add(command)

            while (currentCommandCategoryNode != null) {
                currentDispatcherCategoryNode = currentDispatcherCategoryNode!!.subcategories.find { it.category == currentCommandCategoryNode?.category }!!

                currentDispatcherCategoryNode.commands.add(command)
                currentCommandCategoryNode = currentCommandCategoryNode.next
            }
        }
    }

    fun registerTransformer(transformer: Transformer<*>) = transformers.putIfAbsent(transformer.type, transformer)
}