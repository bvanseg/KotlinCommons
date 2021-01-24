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

import bvanseg.kotlincommons.command.annotation.Command
import bvanseg.kotlincommons.command.context.Context
import bvanseg.kotlincommons.command.exception.*
import bvanseg.kotlincommons.command.gear.Gear
import bvanseg.kotlincommons.command.transformer.Transformer
import bvanseg.kotlincommons.command.util.Union
import bvanseg.kotlincommons.kclass.getKClass
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
open class InternalCommand(
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
    val examples: (Any?) -> String
    private val rawArgs: Boolean

    val data: HashMap<KClass<*>, Annotation> = HashMap()

    inline fun <reified T : Annotation> getData(): T? = data[T::class] as T?

    init {
        val annotation = function.findAnnotation<Command>()

        fun createUsage(usageIn: Array<String>): (Any?) -> String {
            val string = if (usageIn.isNotEmpty())
                usageIn.joinToString("\n") { it.replace("<NAME>", function.name) }
            else {
                function.parameters
                    .filter { !it.type.isSubtypeOf(Context::class.createType()) && !it.type.isSubtypeOf(Gear::class.createType()) && it.name != null }
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

        fun createExamples(examplesIn: Array<String>): (Any?) -> String {
            val string = examplesIn.joinToString("\n") { it.replace("<NAME>", function.name) }
            return { string.replace("<PREFIX>", commandManager.getPrefix(it)) }
        }

        fun registerAliases(aliases: Array<String>) = aliases.forEach { alias ->
            if (alias.isNotBlank()) {
                val lowerAlias = if (commandManager.capsInsensitive) alias.toLowerCase() else alias
                commandManager.commandModules[lowerAlias] = commandModule
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
                throw InvalidParameterException("Command $function has invalid number of parameters for rawArgs")
            val argParamIndex = if (hasContext) paramStart + 1 else paramStart
            when (params[argParamIndex].type.getKClass()) {
                String::class, Array<String>::class -> Unit // No action necessary
                List::class -> {
                    params[argParamIndex].type.arguments[0].type?.let {
                        if (it.getKClass() != String::class)
                            throw InvalidParameterException("Command $function has invalid parameter: ${params[argParamIndex]}")
                    }
                }
                else -> throw InvalidParameterException("Command $function has invalid parameter: ${params[argParamIndex]}")
            }
        }

        if (annotation != null) {
            description = annotation.description
            aliases = annotation.aliases.toList()
            usage = createUsage(annotation.usage)
            examples = createExamples(annotation.examples)
            registerAliases(annotation.aliases)
            rawArgs = annotation.rawArgs
        } else {
            description = baseClass!!.description
            aliases = baseClass.aliases
            usage = createUsage(baseClass.usage.toTypedArray())
            examples = createExamples(baseClass.examples.toTypedArray())
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
        if (!gear.isEnabled) return 0

        // Loop through the commands
        val pArgs: HashMap<String, Any?> = HashMap()

        val params = function.valueParameters
        val lastParam = params.lastOrNull()
        val isLastParamOverflow = lastParam?.type?.let { type ->
            type.getKClass().let { kClass ->
                kClass.isSubclassOf(String::class)
                        || kClass.isSubclassOf(Array<String>::class)
                        || (kClass.isSubclassOf(List::class) && type.arguments[0].type!!.getKClass()
                    .isSubclassOf(String::class))
            }
        } ?: false
        val argsList = if (args.isBlank()) mutableListOf() else args.split(' ').toMutableList()

        // If there are more arguments than there are parameters
        if (!isLastParamOverflow && argsList.size > params.size && !rawArgs)
            throw UnknownParameterException(context, "There are more arguments given than the command allows for!")

        val firstParam = params.firstOrNull()
        val hasContext = firstParam?.type?.getKClass()?.isSubclassOf(Context::class) ?: false
        if (hasContext)
            pArgs[firstParam?.name!!] = context

        if (argsList.isNotEmpty()) {
            if (rawArgs) {
                val startIndex = if (hasContext) 1 else 0
                val argParam = params[startIndex]
                val argParamName = argParam.name!!
                pArgs[argParamName] = when (argParam.type.getKClass()) {
                    String::class -> args
                    Array<String>::class -> argsList.toTypedArray()
                    List::class -> argsList
                    else -> throw InvalidParameterException("Command $function has invalid parameter: $argParam")
                }
            } else {
                if (firstParam?.type?.isSubtypeOf(Context::class.createType()) == true)
                    argsList.add(0, "")
                val paramSize = params.size
                for ((index, arg) in argsList.withIndex()) {

                    if (index > paramSize - 1)
                        continue

                    val parameter = params[index]
                    if (parameter.type.isSubtypeOf(Context::class.createType()))
                        continue

                    val paramType = parameter.type.getKClass()
                    if (index == paramSize - 1) {
                        // If last
                        val subList = argsList.subList(index, argsList.lastIndex + 1)

                        @Suppress("IMPLICIT_CAST_TO_ANY")
                        val value = when (paramType) {
                            String::class -> subList.joinToString(" ")
                            Array<String>::class -> subList.toTypedArray()
                            List::class -> subList.toList()
                            else -> null
                        }
                        if (value != null) {
                            pArgs[parameter.name!!] = value
                            continue
                        }
                    }

                    when (paramType) {
                        Union::class -> {
                            val paramArgs = function.findParameterByName(parameter.name!!)!!.type.arguments
                            pArgs[parameter.name!!] = Union(
                                commandManager,
                                context,
                                arg,
                                paramArgs[0].type!!.getKClass(),
                                paramArgs[1].type!!.getKClass()
                            )
                        }
                        else -> {
                            val transformer: Transformer<*>? =
                                commandManager.transformers[parameter.type.getKClass()]
                            if (transformer != null) {
                                // If this is the last arg but we have more to go?
                                val input = if (index == paramSize - 1) argsList.joinStrings(index) else arg
                                try {
                                    if (input.isNotBlank()) {
                                        pArgs[parameter.name!!] = transformer.parse(parameter, input, context)

                                        if (pArgs[parameter.name!!] == null)
                                            throw TransformerParseException(
                                                context,
                                                "Parsing failed for transforming argument of type ${parameter.type.getKClass()}, the returned value was null! Input: $input"
                                            )
                                    }
                                } catch (e: Exception) {
                                    throw TransformerParseException(
                                        context,
                                        "Parsing failed for transforming argument of type ${parameter.type.getKClass()}! Input: $input"
                                    )
                                }
                            } else
                                throw MissingTransformerException(
                                    context,
                                    "No transformer for the type ${parameter.type.getKClass()} exists!"
                                )
                        }
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
        if (!gear.isEnabled) return 0

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
    internal open fun callNamed(params: Map<String, Any?>, self: Any? = null, extSelf: Any? = null): Any? {
        val map = function.parameters
            .filter { params.containsKey(it.name) }
            .associateWithTo(HashMap()) { params[it.name] }
        if (baseClass == null)
            map[function.instanceParameter!!] = gear
        else
            map[function.instanceParameter!!] = baseClass

        self?.let { map[function.instanceParameter!!] = it }
        extSelf?.let { map[function.extensionReceiverParameter!!] = it }

        val returnObject: Any?
        try {
            returnObject = function.callBy(map)
        } catch (e: IllegalArgumentException) {
            val missingParameters = mutableListOf<String>()
            val requiredParameters = function.parameters.filter { !it.isOptional && it.name != null }.map { it.name }
            for (requiredParameter in requiredParameters)
                if (!params.keys.contains(requiredParameter))
                    missingParameters.add(requiredParameter!!)

            if (missingParameters.isNotEmpty())
                throw MissingParameterException(
                    missingParameters,
                    "Missing required parameter${if (missingParameters.size > 1) "s" else ""}: ${
                        missingParameters.joinToString(", ")
                    }"
                )

            val allParameters = function.parameters.filter { it.name != null }

            val invalidParameters = mutableListOf<String>()
            for (paramName in params.keys) {
                val workingParameterValue = (params[paramName] ?: Unit)::class
                val expectedParameterType = allParameters.find { it.name == paramName }!!.type.getKClass()

                if (workingParameterValue != expectedParameterType)
                    invalidParameters.add("$paramName (expected: ${expectedParameterType.simpleName})")

                if (invalidParameters.isNotEmpty())
                    throw InvalidParameterException(
                        "Invalid types for given parameter${if (invalidParameters.size > 1) "s" else ""}: ${
                            invalidParameters.joinToString(
                                ", "
                            )
                        }"
                    )
            }

            throw UnknownCommandException(e.message ?: "An unknown command exception occurred!")
        }

        return returnObject
    }
}