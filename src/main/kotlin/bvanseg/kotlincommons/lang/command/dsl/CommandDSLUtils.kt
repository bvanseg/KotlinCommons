package bvanseg.kotlincommons.lang.command.dsl

import bvanseg.kotlincommons.lang.check.Check
import bvanseg.kotlincommons.lang.check.Checks

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
fun <T: Any> command(name: String, vararg aliases: String, commandCallback: DSLCommand<T>.() -> Unit): DSLCommand<T> {
    Check.all(name, "command", Checks.notBlank, Checks.noWhitespace)
    aliases.forEach { Check.all(it, "alias", Checks.notBlank, Checks.noWhitespace) }

    val dslCommand = DSLCommand<T>(name, aliases.toMutableList())
    dslCommand.apply(commandCallback)
    return dslCommand
}