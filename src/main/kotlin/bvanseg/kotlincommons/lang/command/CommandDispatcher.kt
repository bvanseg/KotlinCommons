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
package bvanseg.kotlincommons.lang.command

import bvanseg.kotlincommons.lang.command.argument.CommandArguments
import bvanseg.kotlincommons.lang.command.category.CommandCategory
import bvanseg.kotlincommons.lang.command.context.CommandContext
import bvanseg.kotlincommons.lang.command.dsl.DSLCommand
import bvanseg.kotlincommons.lang.command.token.TokenParser
import bvanseg.kotlincommons.lang.command.transformer.Transformer
import bvanseg.kotlincommons.lang.command.transformer.impl.ArgumentTokenBufferTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.BigDecimalTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.BigIntegerTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.ColorTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.StringBuilderTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.TokenTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.URLTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.UUIDTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.primitive.BooleanTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.primitive.ByteTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.primitive.CharTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.primitive.DoubleTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.primitive.FloatTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.primitive.IntTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.primitive.LongTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.primitive.ShortTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.primitive.UByteTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.primitive.UIntTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.primitive.ULongTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.primitive.UShortTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.time.ChronoUnitTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.time.DayOfWeekTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.time.InstantTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.time.KhronoTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.time.KhronoUnitTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.time.LocalDateTimeTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.time.LocalDateTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.time.LocalTimeTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.time.MonthTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.time.OffsetDateTimeTransformer
import bvanseg.kotlincommons.lang.command.transformer.impl.time.TimeUnitTransformer
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import kotlin.reflect.KClass

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
class CommandDispatcher(val prefix: String) {

    companion object {
        val ROOT_CATEGORY = CommandCategory("Root", "*")
    }

    private val commands: ConcurrentMap<String, DSLCommand<out Any>> = ConcurrentHashMap()
    private val categories: ConcurrentMap<CommandCategory, MutableList<DSLCommand<out Any>>> = ConcurrentHashMap()

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

        registerTransformer(UByteTransformer)
        registerTransformer(UShortTransformer)
        registerTransformer(UIntTransformer)
        registerTransformer(ULongTransformer)

        registerTransformer(ArgumentTokenBufferTransformer)
        registerTransformer(BigDecimalTransformer)
        registerTransformer(BigIntegerTransformer)
        registerTransformer(ColorTransformer)
        registerTransformer(InstantTransformer)
        registerTransformer(KhronoTransformer)
        registerTransformer(LocalDateTimeTransformer)
        registerTransformer(LocalDateTransformer)
        registerTransformer(LocalTimeTransformer)
        registerTransformer(Number::class, DoubleTransformer)
        registerTransformer(OffsetDateTimeTransformer)
        registerTransformer(StringBuilderTransformer)
        registerTransformer(TokenTransformer)
        registerTransformer(URLTransformer)
        registerTransformer(UUIDTransformer)

        registerTransformer(ChronoUnitTransformer)
        registerTransformer(DayOfWeekTransformer)
        registerTransformer(KhronoUnitTransformer)
        registerTransformer(MonthTransformer)
        registerTransformer(TimeUnitTransformer)
    }

    fun execute(input: String, commandContext: CommandContext = CommandContext(this)) =
        execute(prefix, input, commandContext)

    fun execute(prefix: String, input: String, commandContext: CommandContext = CommandContext(this)): Any? {
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

    fun getCommandByName(name: String): DSLCommand<out Any>? = commands[name]
    fun getCommandsByCategoryPath(path: String): List<DSLCommand<out Any>> =
        categories[CommandCategory("", path)] ?: emptyList()

    fun getCategories(): Map<CommandCategory, List<DSLCommand<out Any>>> = categories
    fun getRootCommands(): List<DSLCommand<out Any>> = categories[ROOT_CATEGORY] ?: emptyList()

    fun registerCommand(command: DSLCommand<out Any>) {
        commands.putIfAbsent(command.name, command)
        command.aliases.forEach { alias ->
            commands.putIfAbsent(alias, command)
        }

        categories.computeIfAbsent(command.category) { mutableListOf() }.add(command)
    }

    fun registerTransformer(transformer: Transformer<*>) = registerTransformer(transformer.type, transformer)
    fun registerTransformer(type: KClass<*>, transformer: Transformer<*>) = transformers.putIfAbsent(type, transformer)
}