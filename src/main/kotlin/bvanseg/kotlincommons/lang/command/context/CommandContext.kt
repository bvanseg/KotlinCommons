package bvanseg.kotlincommons.lang.command.context

import bvanseg.kotlincommons.lang.command.CommandDispatcher
import bvanseg.kotlincommons.lang.command.dsl.key.DSLFlagKey
import bvanseg.kotlincommons.lang.command.dsl.key.DSLKey
import bvanseg.kotlincommons.lang.command.token.Token

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
open class CommandContext(val dispatcher: CommandDispatcher) {

    lateinit var rawInput: String
    lateinit var splitRawInput: List<String>
    lateinit var tokenizedArguments: List<Token>

    private val argumentMap: HashMap<String, Any> = hashMapOf()
    private val flagSet: HashSet<String> = hashSetOf()

    fun <T: Any> getArgument(key: DSLKey<T>): T = getArgument(key.name)
    fun <T: Any> getArgument(name: String): T = argumentMap[name] as T
    fun setArgument(name: String, value: Any) = argumentMap.putIfAbsent(name, value)

    fun hasFlag(key: DSLFlagKey): Boolean = hasFlag(key.name, *key.names.toTypedArray())
    fun hasFlag(name: String, vararg names: String): Boolean {
        if (flagSet.contains(name)) return true
        for (n in names) {
            if (flagSet.contains(n)) {
                return true
            }
        }
        return false
    }
    fun addFlag(name: String) = flagSet.add(name)
}