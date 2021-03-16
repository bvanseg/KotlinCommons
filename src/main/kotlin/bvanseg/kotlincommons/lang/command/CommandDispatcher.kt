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

import bvanseg.kotlincommons.io.logging.debug
import bvanseg.kotlincommons.io.logging.getLogger
import bvanseg.kotlincommons.io.logging.info
import bvanseg.kotlincommons.io.logging.warn
import bvanseg.kotlincommons.lang.command.argument.CommandArguments
import bvanseg.kotlincommons.lang.command.context.CommandContext
import bvanseg.kotlincommons.lang.command.dsl.DSLCommand
import bvanseg.kotlincommons.lang.command.event.CommandFireEvent
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
import bvanseg.kotlincommons.util.event.EventBus
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import kotlin.reflect.KClass

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
class CommandDispatcher(val prefix: String) {

    companion object {
        val logger = getLogger()
    }

    private val commands: ConcurrentMap<String, DSLCommand> = ConcurrentHashMap()

    val eventBus = EventBus()

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
        logger.info { "Executing command with input '$input'..." }
        if (input.isBlank()) return null
        if (!input.startsWith(prefix)) return null
        val trimmedInput = input.trim()

        val parts = trimmedInput.split(" ", limit = 2)
        val commandReference = parts[0]
        val commandName = commandReference.substring(prefix.length, commandReference.length)

        val command = commands[commandName]

        if (command == null) {
            logger.debug { "Attempted to execute input for command '$commandName' but no such command exists." }
            return null
        }

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

        val commandArguments = CommandArguments(this, command, commandContext)
        commandArguments.parse(commandContext.tokenizedArguments)

        eventBus.fire(CommandFireEvent.PRE(command, commandContext, this))
        val result = command.run(commandArguments, commandContext)
        eventBus.fire(CommandFireEvent.POST(command, commandContext, this))

        return result
    }

    fun getCommandByName(name: String): DSLCommand? = commands[name]

    fun registerCommand(command: DSLCommand) {
        commands.compute(command.name) { _, cmd ->
            if (cmd != null) {
                logger.warn { "Attempting to register a command under name '${command.name}' but a command under that name already exists!" }
            }
            return@compute command
        }
        command.aliases.forEach { alias ->
            commands.compute(alias) { _, aliasedCommand ->
                if (aliasedCommand != null) {
                    logger.warn { "Attempting to register a command with name '${command.name}' under alias '$alias' but a command under that alias already exists!" }
                }
                return@compute command
            }
        }
        logger.info { "Registered command with name '${command.name}' and aliases ${command.aliases.joinToString { "'$it'" }}." }
    }

    fun registerTransformer(transformer: Transformer<*>, overwrite: Boolean = false) = registerTransformer(transformer.type, transformer, overwrite)
    fun registerTransformer(type: KClass<*>, transformer: Transformer<*>, overwrite: Boolean = false) = transformers.compute(type) { _, value ->
        if (value != null && !overwrite) {
            logger.warn { "Attempted to register a transformer for type '$type' but it already exists! Set 'overwrite' to 'true' during registration to overwrite the existing transformer." }
            return@compute value
        }
        logger.info { "Registered transformer for type '$type'." }
        transformer
    }
}