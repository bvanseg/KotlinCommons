/*
 * MIT License
 *
 * Copyright (c) 2019 Boston Vanseghi
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
package bvanseg.kotlincommons.armada

import bvanseg.kotlincommons.any.getLogger
import bvanseg.kotlincommons.armada.annotations.*
import bvanseg.kotlincommons.armada.commands.BaseCommand
import bvanseg.kotlincommons.armada.commands.CommandModule
import bvanseg.kotlincommons.armada.commands.InternalCommand
import bvanseg.kotlincommons.armada.contexts.Context
import bvanseg.kotlincommons.armada.contexts.EmptyContext
import bvanseg.kotlincommons.armada.events.*
import bvanseg.kotlincommons.armada.gears.Gear
import bvanseg.kotlincommons.armada.transformers.*
import bvanseg.kotlincommons.evenir.bus.EventBus
import kotlin.collections.set
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
class CommandManager<T>(val prefix: String = "!") {
    private val log = getLogger()
    var capsInsensitive = true
    val commandModules: HashMap<String, CommandModule> = HashMap()
    val gears: ArrayList<Gear> = ArrayList()
    val transformers: HashMap<KClass<*>, Transformer<*>> = HashMap()
    // Stores alternative prefixes to a key of the developer's desired type.
    val prefixes: HashMap<T, String> = HashMap()
    // Used internally for removing whitespace on commands.
    private val whitespaceRegex = Regex("\\s+")
    // The event bus used for handling Armada's events.
    val eventBus = EventBus()

    init {
        val e = Init()
        eventBus.fire(e)
        if (!e.isCancelled)
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
                TimeUnitTransformer
            )
    }

    companion object {
        val ranges = listOf(
            ByteRange::class,
            ShortRange::class,
            IntRange::class,
            FloatRange::class,
            DoubleRange::class,
            LongRange::class
        )
    }

    val aliasMap = hashMapOf<String, String>()

    /**
     * Strips the prefix away from the front of raw command input.
     */
    private fun stripPrefix(rawCommand: String, tempPrefix: String = prefix): String = rawCommand.substringAfter(tempPrefix)

    /**
     * Fetches the prefix that is registered under the specified key. If no such key exists, uses the command manager's default prefix, instead.
     */
    fun getPrefix(key: T? = null): String = key?.let { prefixes.getOrDefault(key, prefix) } ?: prefix

    /**
     * Executes raw command input, turning it into an @link{InternalCommand}. If the command function returns any value, this function will
     * also return the value from the function invocation.
     */
    fun execute(rawCommand: String, context: Context = EmptyContext, key: T? = null): Any? {
        val tempPrefix = if(key != null) (prefixes[key] ?: prefix) else prefix
        if (!rawCommand.startsWith(tempPrefix))
            return null

        val commandNameAndArgs = stripPrefix(rawCommand, tempPrefix).trim().split(whitespaceRegex, limit = 2)
        var commandName = commandNameAndArgs[0]
        log.debug("Receiving command: $commandName with prefix $tempPrefix")

        if (capsInsensitive)
            commandName = commandName.toLowerCase()

        if(aliasMap[commandName] != null)
            commandName = aliasMap[commandName]!!

        commandModules[commandName]?.let {
            val args = if (commandNameAndArgs.size == 1) "" else commandNameAndArgs[1]
            log.debug("Executing command ($commandName) from CommandModule (${it.tag})")
            val command = it.findCandidateCommand(args, context)
            command?.let { cmd ->
                val pre = CommandExecuteEvent.Pre(this, cmd, context)
                eventBus.fire(pre)
                if (pre.isCancelled)
                    return null
                val result = cmd.invoke(args, context)
                eventBus.fire(CommandExecuteEvent.Post(this, cmd, context, result))
                return result
            }
        }
        log.debug("Command $commandName does not exist!")
        return null
    }

    /**
     * Fetches a {@link CommandModule} from raw command input. Returns null if no such module is found.
     */
    fun getCommandModule(rawCommand: String, key: T? = null): CommandModule? {
        var command =
            if(key == null || prefixes[key] == null)
                stripPrefix(rawCommand).substringBefore(' ').trim()
            else
                stripPrefix(rawCommand, prefixes[key]!!).substringBefore(' ').trim()

        if (capsInsensitive)
            command = command.toLowerCase()
        return commandModules[command]
    }

    /**
     * Fetches a {@link Gear} by its name. Returns null if no such gear is found.
     */
    fun getGear(gearName: String): Gear? = gears.find { it.name.equals(gearName, true) }

    /**
     * Registers a {@link Gear} instance's class to the command manager.
     */
    fun addCommand(command: BaseCommand) {
        val gear = this.getGear(command.gear) ?: return
        val pre = CommandAddEvent.Pre(command, this)
        val post = CommandAddEvent.Post(command, this)
        eventBus.fire(pre)
        if(pre.isCancelled) return
        command::class.memberFunctions.filter { it.findAnnotation<Invoke>() != null }.forEach {
            val com = InternalCommand(this, it, gear, command)
            for(annotation in com.function.annotations) {
                if(annotation.annotationClass != Command::class) {
                    com.data[annotation.annotationClass] = annotation
                }
            }
            var methodName = it.name
            if(capsInsensitive)
                methodName = methodName.toLowerCase()

            if(commandModules[methodName] == null) {
                commandModules[methodName] = CommandModule(methodName, this)
            }
            commandModules[methodName]?.commands?.add(com)
            gear.commands.add(com)
            eventBus.fire(post)
            log.debug("Registered base command (${com.name}) for gear (${gear::class})")
        }
    }

    /**
     * Registers a {@link Gear} to the command manager by its class.
     */
    fun addGear(type: KClass<out Gear>) =
        addGear(
            (if (type.isCompanion) type.companionObjectInstance else type.objectInstance
                ?: type.createInstance()) as Gear
        )

    /**
     * Registers a {@link Gear} instance's class to the command manager.
     */
    fun addGear(gear: Gear) {
        val pre = GearAddEvent.Pre(gear, this)
        val post = GearAddEvent.Post(gear, this)
        eventBus.fire(pre)
        if(pre.isCancelled) return
        log.debug("Registering gear ${gear::class}...")
        gear.commandManager = this
        gears.add(gear)
        gear::class.memberFunctions.filter { it.findAnnotation<Command>() != null }.forEach { method ->
            val command = InternalCommand(this, method, gear)
            for (annotation in command.function.annotations) {
                if (annotation.annotationClass != Command::class) {
                    // Attach data to command
                    command.data[annotation.annotationClass] = annotation
                }
            }
            var methodName = method.name
            if (capsInsensitive)
                methodName = methodName.toLowerCase()

            if(commandModules[methodName] == null) {
                commandModules[methodName] = CommandModule(methodName, this)
            }
            commandModules[methodName]?.commands?.add(command)
            gear.commands.add(command)
            log.debug("Registered command (${command.name}) for gear (${gear::class})")
        }
        eventBus.fire(post)
        log.debug("Successfully registered gear (${gear::class})")
    }

    /**
     * Registers a {@link Transformer} to the command manager.
     */
    fun <T : Any> addTransformer(transformer: Transformer<T>) {
        val pre = TransformerAddEvent.Pre(transformer, this)
        val post = TransformerAddEvent.Post(transformer, this)
        eventBus.fire(pre)
        if(pre.isCancelled) return
        transformers[transformer.type] = transformer
        eventBus.fire(post)
        log.debug("Registered transformer with type (${transformer.type})")
    }

    /**
     * Registers a variable number of {@link Transformer}s to the command manager.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun addTransformers(vararg transformers: Transformer<*>) = transformers.forEach { addTransformer(it) }
}