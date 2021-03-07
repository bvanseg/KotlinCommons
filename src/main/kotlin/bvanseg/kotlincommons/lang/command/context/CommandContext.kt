package bvanseg.kotlincommons.lang.command.context

import bvanseg.kotlincommons.lang.command.CommandDispatcher
import bvanseg.kotlincommons.lang.command.dsl.DSLKey

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
abstract class CommandContext(val dispatcher: CommandDispatcher) {

    lateinit var rawInput: String
    lateinit var splitRawInput: List<String>
    lateinit var splitArguments: List<String>

    private val argumentMap: HashMap<String, Any> = hashMapOf()

    fun <T: Any> get(key: DSLKey<T>): T = argumentMap[key.name] as T
    fun set(name: String, value: Any) = argumentMap.putIfAbsent(name, value)
}