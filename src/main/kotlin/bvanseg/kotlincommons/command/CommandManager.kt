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
package bvanseg.kotlincommons.command

import bvanseg.kotlincommons.any.getLogger
import bvanseg.kotlincommons.command.annotation.Command
import bvanseg.kotlincommons.command.annotation.Invoke
import bvanseg.kotlincommons.command.context.Context
import bvanseg.kotlincommons.command.context.EmptyContext
import bvanseg.kotlincommons.command.event.CommandAddEvent
import bvanseg.kotlincommons.command.event.CommandExecuteEvent
import bvanseg.kotlincommons.command.event.CommandManagerInitializationEvent
import bvanseg.kotlincommons.command.event.GearAddEvent
import bvanseg.kotlincommons.command.event.TransformerAddEvent
import bvanseg.kotlincommons.command.event.ValidatorAddEvent
import bvanseg.kotlincommons.command.exception.DuplicateTransformerException
import bvanseg.kotlincommons.command.exception.DuplicateValidatorException
import bvanseg.kotlincommons.command.gear.Gear
import bvanseg.kotlincommons.command.transformer.ArgumentTransformer
import bvanseg.kotlincommons.command.transformer.BigDecimalTransformer
import bvanseg.kotlincommons.command.transformer.BigIntegerTransformer
import bvanseg.kotlincommons.command.transformer.BooleanTransformer
import bvanseg.kotlincommons.command.transformer.ByteTransformer
import bvanseg.kotlincommons.command.transformer.CharTransformer
import bvanseg.kotlincommons.command.transformer.DoubleTransformer
import bvanseg.kotlincommons.command.transformer.FloatTransformer
import bvanseg.kotlincommons.command.transformer.IntRangeTransformer
import bvanseg.kotlincommons.command.transformer.IntTransformer
import bvanseg.kotlincommons.command.transformer.LongRangeTransformer
import bvanseg.kotlincommons.command.transformer.LongTransformer
import bvanseg.kotlincommons.command.transformer.ShortTransformer
import bvanseg.kotlincommons.command.transformer.StringTransformer
import bvanseg.kotlincommons.command.transformer.TimeUnitTransformer
import bvanseg.kotlincommons.command.transformer.Transformer
import bvanseg.kotlincommons.command.transformer.UIntRangeTransformer
import bvanseg.kotlincommons.command.transformer.ULongRangeTransformer
import bvanseg.kotlincommons.command.transformer.URITransformer
import bvanseg.kotlincommons.command.transformer.URLTransformer
import bvanseg.kotlincommons.command.validator.Validator
import bvanseg.kotlincommons.command.validator.impl.*
import bvanseg.kotlincommons.event.bus.EventBus
import kotlin.collections.set
import kotlin.ranges.IntRange
import kotlin.ranges.LongRange
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberFunctions

/**
 * Responsible for handling all prefixes, commands, command modules, gears, and transformers.
 *
 * @author Boston Vanseghi
 * @since 2.1.0
 */
class CommandManager<T : Any>(val prefix: String = "!") {

    companion object {
        val logger = getLogger()
    }

    var capsInsensitive = true
    val commandModules = hashMapOf<String, CommandModule>()
    val gears = arrayListOf<Gear>()
    val transformers = hashMapOf<KClass<*>, Transformer<*>>()
    val validators = hashMapOf<KClass<out Annotation>, List<Validator<*, *>>>()

    // Stores alternative prefixes to a key of the developer's desired type.
    val prefixes = hashMapOf<T, String>()

    // Used internally for removing whitespace on commands.
    private val whitespaceRegex = Regex("\\s+")

    // The event bus used for handling the command manager's events.
    val eventBus = EventBus()

    init {
        val e = CommandManagerInitializationEvent()
        eventBus.fire(e)
        if (!e.isCancelled) {
            addTransformers(
                IntTransformer,
                DoubleTransformer,
                FloatTransformer,
                LongTransformer,
                ShortTransformer,
                ByteTransformer,
                CharTransformer,
                BooleanTransformer,
                StringTransformer,
                ArgumentTransformer,
                BigIntegerTransformer,
                BigDecimalTransformer,
                TimeUnitTransformer,
                IntRangeTransformer,
                LongRangeTransformer,
                UIntRangeTransformer,
                ULongRangeTransformer,
                URITransformer,
                URLTransformer
            )

            addValidators(ClampByte::class, ClampByteValidator)
            addValidators(ClampShort::class, ClampShortValidator)
            addValidators(ClampInt::class, ClampIntValidator)
            addValidators(ClampLong::class, ClampLongValidator)
            addValidators(ClampFloat::class, ClampFloatValidator)
            addValidators(ClampDouble::class, ClampDoubleValidator)
        }
    }

    /**
     * Fetches the prefix that is registered under the specified key. If no such key exists, uses the command manager's default prefix, instead.
     */
    fun getPrefix(key: T? = null): String = key?.let { prefixes.getOrDefault(it, prefix) } ?: prefix

    /**
     * Strips the prefix away from the front of raw command input.
     */
    fun stripPrefix(rawCommand: String, prefix: String): String = rawCommand.substringAfter(prefix)

    /**
     * Strips the prefix away from the front of raw command input.
     */
    fun stripPrefix(rawCommand: String, key: T? = null): String = stripPrefix(rawCommand, getPrefix(key))

    /**
     * Splits the [command] in two parts, where the first is just the command name and the second are the arguments.
     */
    fun splitCommand(command: String): Pair<String, String> =
        command.trim().split(whitespaceRegex, 2).let { it.getOrElse(0) { "" } to it.getOrElse(1) { "" } }

    /**
     * Extracts just the command name from the given raw [command].
     * Uses the given [key] to get the correct prefix to strip from the beginning.
     */
    fun extractCommandName(command: String, key: T? = null): String =
        stripPrefix(command, key).trim().split(whitespaceRegex, 2)[0]

    /**
     * Executes raw command input, turning it into an [InternalCommand]. If the command function returns any value, this function will
     * also return the value from the function invocation.
     */
    fun execute(rawCommand: String, context: Context = EmptyContext, key: T? = null): Any? {
        val commandPrefix = getPrefix(key)
        if (!rawCommand.startsWith(commandPrefix)) return null

        val commandNameAndArgs = splitCommand(stripPrefix(rawCommand, commandPrefix))
        var commandName = commandNameAndArgs.first
        logger.debug("Receiving command: {} with prefix {}", commandName, commandPrefix)

        if (capsInsensitive) {
            commandName = commandName.toLowerCase()
        }

        commandModules[commandName]?.let {
            val args = commandNameAndArgs.second
            logger.debug("Executing command ({}) from CommandModule ({})", commandName, it.tag)
            val command = it.findCandidateCommand(args, context)
            command?.let { cmd ->
                val pre = CommandExecuteEvent.Pre(this, cmd, context)
                eventBus.fire(pre)

                if (pre.isCancelled) return null

                val result = cmd.invoke(args, context)
                eventBus.fire(CommandExecuteEvent.Post(this, cmd, context, result))
                return result
            }
        }
        logger.debug("Command {} does not exist!", commandName)
        return null
    }

    /**
     * Fetches a [CommandModule] from raw command input. Returns null if no such module is found.
     */
    fun getCommandModule(rawCommand: String, key: T? = null): CommandModule? {
        var command =
            if (key == null || prefixes[key] == null) {
                stripPrefix(rawCommand).substringBefore(' ').trim()
            } else {
                stripPrefix(rawCommand, prefixes[key]!!).substringBefore(' ').trim()
            }

        if (capsInsensitive) {
            command = command.toLowerCase()
        }

        return commandModules[command]
    }

    /**
     * Fetches a [Gear] by its name. Returns null if no such gear is found.
     */
    fun getGear(gearName: String): Gear? = gears.find { it.name.equals(gearName, true) }

    /**
     * Registers a [Gear] instance's class to the command manager.
     */
    fun addCommand(command: BaseCommand) {
        val gear = this.getGear(command.gear) ?: return
        val pre = CommandAddEvent.Pre(command, this)
        val post = CommandAddEvent.Post(command, this)
        eventBus.fire(pre)
        if (pre.isCancelled) return
        command::class.memberFunctions.filter { it.findAnnotation<Invoke>() != null }.forEach { function ->
            var functionName = function.name

            if (capsInsensitive) {
                functionName = functionName.toLowerCase()
            }

            if (commandModules[functionName] == null) {
                commandModules[functionName] = CommandModule(functionName, this)
            }

            val module = commandModules[functionName]!!

            @Suppress("UNCHECKED_CAST")
            val com = InternalCommand(this as CommandManager<Any>, module, function, gear, command)

            for (annotation in com.function.annotations) {
                if (annotation.annotationClass != Command::class) {
                    com.data[annotation.annotationClass] = annotation
                }
            }

            module.commands.add(com)
            gear.commands.add(com)
            eventBus.fire(post)
            logger.debug("Registered base command ({}) for gear ({})", com.name, gear::class)
        }
    }

    /**
     * Registers a [Gear] to the command manager by its class.
     */
    fun addGear(type: KClass<out Gear>) =
        addGear(
            (if (type.isCompanion) type.companionObjectInstance else type.objectInstance
                ?: type.createInstance()) as Gear
        )

    /**
     * Registers a [Gear] instance's class to the command manager.
     */
    fun addGear(gear: Gear) {
        val pre = GearAddEvent.Pre(gear, this)
        val post = GearAddEvent.Post(gear, this)
        eventBus.fire(pre)
        if (pre.isCancelled) return
        logger.debug("Registering gear {}...", gear::class)
        gear.commandManager = this
        gears.add(gear)
        gear::class.memberFunctions.filter { it.findAnnotation<Command>() != null }.forEach { function ->
            var functionName = function.name

            if (capsInsensitive) {
                functionName = functionName.toLowerCase()
            }

            if (commandModules[functionName] == null) {
                commandModules[functionName] = CommandModule(functionName, this)
            }

            val module = commandModules[functionName]!!

            @Suppress("UNCHECKED_CAST")
            val command = InternalCommand(this as CommandManager<Any>, module, function, gear)

            for (annotation in command.function.annotations) {
                if (annotation.annotationClass != Command::class) {
                    command.data[annotation.annotationClass] = annotation
                }
            }

            for (annotation in gear::class.annotations) {
                command.data[annotation.annotationClass] = annotation
            }

            module.commands.add(command)
            gear.commands.add(command)
            logger.debug("Registered command ({}) for gear ({})", command.name, gear::class)
        }

        eventBus.fire(post)
        logger.debug("Successfully registered gear ({})", gear::class)
    }

    /**
     * Registers a [Transformer] to the command manager.
     */
    fun <T : Any> addTransformer(transformer: Transformer<T>) {
        val pre = TransformerAddEvent.Pre(transformer, this)
        val post = TransformerAddEvent.Post(transformer, this)
        eventBus.fire(pre)
        if (pre.isCancelled) return

        if (transformers[transformer.type] != null) {
            throw DuplicateTransformerException("Transformer of type ${transformer.type.qualifiedName} is already registered!")
        }

        transformers[transformer.type] = transformer
        eventBus.fire(post)
        logger.debug("Registered transformer with type ({})", transformer.type)
    }

    /**
     * Registers a variable number of [Transformer]s to the command manager.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun addTransformers(vararg transformers: Transformer<*>) = transformers.forEach { addTransformer(it) }


    /**
     * Registers a [Validator] to the command manager.
     */
    fun addValidators(annotationClass: KClass<out Annotation>, vararg validatorsToAdd: Validator<*, *>) {
        val validatorsList = validatorsToAdd.toList()
        val pre = ValidatorAddEvent.Pre(annotationClass, validatorsList, this)
        val post = ValidatorAddEvent.Post(annotationClass, validatorsList, this)
        eventBus.fire(pre)

        if (pre.isCancelled) return

        if (validators[annotationClass] != null) {
            throw DuplicateValidatorException("Validator of type $annotationClass is already registered!")
        }

        validators[annotationClass] = validatorsList
        eventBus.fire(post)
        logger.debug("Registered validator with type ({})", annotationClass)
    }
}