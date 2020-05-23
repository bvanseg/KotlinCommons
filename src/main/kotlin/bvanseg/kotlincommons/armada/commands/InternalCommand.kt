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
package bvanseg.kotlincommons.armada.commands

import bvanseg.kotlincommons.armada.CommandManager
import bvanseg.kotlincommons.armada.annotations.Command
import bvanseg.kotlincommons.armada.contexts.Context
import bvanseg.kotlincommons.armada.gears.Gear
import bvanseg.kotlincommons.armada.transformers.Transformer
import bvanseg.kotlincommons.armada.utilities.Union
import bvanseg.kotlincommons.kclasses.getKClass
import bvanseg.kotlincommons.string.joinStrings
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.*

/**
 * An object representation of a {@link Command}. Hard/light invocation of the function is handled here.
 *
 * @author Boston Vanseghi
 * @since 2.1.0
 */
class InternalCommand(
    val commandManager: CommandManager<in Any>,
    val commandModule: CommandModule,
    val function: KFunction<*>,
    val gear: Gear,
    val baseClass: BaseCommand? = null
) {

    val name = function.name
    val aliases: List<String>
    val description: String
    // The input arg should be the same as used in CommandManager#getPrefix
    val usage: (Any?) -> String
    private val rawArgs: Boolean

    val data: HashMap<KClass<*>, Annotation> = HashMap()

    inline fun <reified T : Annotation> getData(): T? = data[T::class] as T?

    init {
        val annotation = function.findAnnotation<Command>()

        fun createUsage(annotationUsage: Array<String>): (Any?) -> String {
            val string = if (annotationUsage.isNotEmpty())
                annotationUsage.joinToString("\n") { "<PREFIX>${function.name} $it" }
            else {
                function.parameters
                    .filter { !it.type.isSubtypeOf(Context::class.createType()) && !it.type.isSubtypeOf(Gear::class.createType()) }
                    .joinToString(" ", "<PREFIX>${function.name} ") {
                        val paramName = it.name!!
                        if (it.isOptional)
                            "<$paramName>"
                        else
                            "($paramName)"
                    }
            }
            return { string.replace("<PREFIX>", commandManager.getPrefix(it)) }
        }

        fun registerAliases(aliases: Array<String>) = aliases.forEach { alias ->
            if(alias.isNotBlank()) {
                val lowerAlias = if(commandManager.capsInsensitive) alias.toLowerCase() else alias
                commandManager.aliasMap[lowerAlias] = function.name
            }
        }

        fun validateRawArgs() {
            val params = function.parameters
            var numParams = params.size
            var paramStart = 0
            if (params[0].kind == KParameter.Kind.INSTANCE) {
                paramStart++
                numParams--
            }
            val hasContext = params[paramStart].type.getKClass().isSubclassOf(Context::class)
            val expectedNumParams = if (hasContext) 2 else 1
            if (numParams > expectedNumParams)
                throw RuntimeException("Command $function has invalid number of parameters for rawArgs")
            val argParamIndex = if (hasContext) paramStart + 1 else paramStart
            when (params[argParamIndex].type.getKClass()) {
                String::class, Array<String>::class -> {}
                List::class -> {
                    params[argParamIndex].type.arguments[0].type?.let {
                        if (it.getKClass() != String::class)
                            throw RuntimeException("Command $function has invalid parameter: ${params[argParamIndex]}")
                    }
                }
                else -> throw RuntimeException("Command $function has invalid parameter: ${params[argParamIndex]}")
            }
        }

        if (annotation != null) {
            description = annotation.description
            aliases = annotation.aliases.toList()
            usage = createUsage(annotation.usage)
            registerAliases(annotation.aliases)
            rawArgs = annotation.rawArgs
        } else {
            description = baseClass!!.description
            aliases = baseClass.aliases
            usage = createUsage(baseClass.usage.toTypedArray())
            registerAliases(baseClass.aliases.toTypedArray())
            rawArgs = baseClass.rawArgs
        }
        if (rawArgs)
            validateRawArgs()
    }

    /**
     * Handles the actual execution of the raw command given arguments.
     * Additionally handles a context if one is present in the command arguments.
     */
    fun invoke(args: String, context: Context): Any? {

        // Is the gear disabled?
        if(!gear.isEnabled) return 0

        // Loop through the commands
        val pArgs: HashMap<String, Any?> = HashMap()

        val params = function.valueParameters
        val firstParam = params.firstOrNull()
        val hasContext = firstParam?.type?.getKClass()?.isSubclassOf(Context::class) ?: false
        if (hasContext)
            pArgs[firstParam?.name!!] = context

        if (args.isNotBlank()) {
            val argsList = args.split(' ').toMutableList()
            if (rawArgs) {
                val startIndex = if (hasContext) 1 else 0
                val argParam = params[startIndex]
                val argParamName = argParam.name!!
                pArgs[argParamName] = when (argParam.type.getKClass()) {
                    String::class -> args
                    Array<String>::class -> argsList.toTypedArray()
                    List::class -> argsList
                    else -> throw RuntimeException("Command $function has invalid parameter: $argParam")
                }
            } else {
                var variableArguments = false
                if (firstParam?.type?.isSubtypeOf(Context::class.createType()) == true)
                    argsList.add(0, "")
                val paramSize = params.size
                loop@ for ((index, arg) in argsList.withIndex()) {

                    if (index > paramSize - 1)
                        continue

                    val parameter = params[index]
                    if (parameter.type.isSubtypeOf(Context::class.createType()))
                        continue

                    val value: Any? = when (parameter.type.getKClass()) {
                        Union::class -> {
                            val paramArgs = function.findParameterByName(parameter.name!!)!!.type.arguments
                            Union(
                                commandManager,
                                context,
                                arg,
                                paramArgs[0].type!!.getKClass(),
                                paramArgs[1].type!!.getKClass()
                            )
                        }
                        Array<String>::class, List::class -> {
                            variableArguments = true
                            break@loop
                        }
                        List::class -> argsList.subList(index, argsList.size).joinToString(" ")
                        else -> {
                            val transformer: Transformer<*>? =
                                commandManager.transformers[parameter.type.getKClass()]
                            if (transformer != null) {
                                // If this is the last arg but we have more to go?
                                if (index == paramSize - 1) {
                                    transformer.parse(parameter, argsList.joinStrings(index), context)
                                }
                                else
                                    transformer.parse(parameter, arg, context)
                            } else
                                continue@loop
                        }
                    }

                    pArgs[parameter.name!!] = value
                }

                if (variableArguments) {
                    val index = paramSize - 1
                    val param = params[index]
                    val subList = argsList.subList(index, argsList.lastIndex + 1)
                    @Suppress("IMPLICIT_CAST_TO_ANY")
                    when (param.type.getKClass()) {
                        Array<String>::class -> subList.toTypedArray()
                        List::class -> subList.toList()
                        else -> null
                    }?.let {
                        pArgs[param.name!!] = it
                    }
                }
            }
        }

        return callNamed(pArgs)
    }

    /**
     * Functions similarly to {@link InternalCommand#invoke()}, but does not actually execute the function.
     * Instead, it tracks arguments and returns a score based on the argument chain. This is used to help a
     * {@link CommandModule} determine what command is best fit to handle a set of arguments.
     */
    fun softInvoke(args: String, context: Context): Int {
        // Is the gear disabled?
        if(!gear.isEnabled) return 0

        val params = function.valueParameters
        val firstParam = params.firstOrNull()

        var score = 0
        if (args.isNotBlank()) {
            val argsList = args.split(' ').toMutableList()
            if (rawArgs) {
                score++
            } else {
                if (firstParam?.type?.isSubtypeOf(Context::class.createType()) == true)
                    argsList.add(0, "")
                val paramSize = params.size
                loop@ for ((index, arg) in argsList.withIndex()) {
                    if (index > paramSize - 1)
                        continue
                    val parameter = params[index]
                    if (parameter.type.isSubtypeOf(Context::class.createType()))
                        continue

                    val type = parameter.type.getKClass()
                    commandManager.transformers[type]?.let { transformer ->
                        transformer.parse(parameter, arg, context)?.let {
                            score += if (type == String::class) 1 else 2
                        }
                    }
                }
            }
        }

        return score
    }

    /**
     * Uses the processed arguments from {@link InternalCommand#invoke()} and executes the wrapped function with them.
     */
    private fun callNamed(params: Map<String, Any?>, self: Any? = null, extSelf: Any? = null): Any? {
        val map = function.parameters
            .filter { params.containsKey(it.name) }
            .associateWithTo(HashMap()) { params[it.name] }
        if(baseClass == null)
            map[function.instanceParameter!!] = gear
        else
            map[function.instanceParameter!!] = baseClass

        self?.let { map[function.instanceParameter!!] = it }
        extSelf?.let { map[function.extensionReceiverParameter!!] = it }
        return function.callBy(map)
    }
}