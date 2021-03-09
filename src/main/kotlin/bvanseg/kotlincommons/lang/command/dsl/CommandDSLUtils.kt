package bvanseg.kotlincommons.lang.command.dsl

import bvanseg.kotlincommons.lang.check.Check
import bvanseg.kotlincommons.lang.check.Checks
import bvanseg.kotlincommons.lang.command.CommandProperties

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
fun <T: CommandProperties> command(name: String, vararg aliases: String, commandCallback: DSLCommand<T>.() -> Unit): DSLCommand<T> {
    Check.all(name, "command", Checks.notBlank, Checks.noWhitespace)
    aliases.forEach { Check.all(it, "alias", Checks.notBlank, Checks.noWhitespace) }

    val dslCommand = DSLCommand<T>(name, aliases.toList())
    dslCommand.apply(commandCallback)
    return dslCommand
}