package bvanseg.kotlincommons.lang.command.dsl

import bvanseg.kotlincommons.lang.check.Check
import bvanseg.kotlincommons.lang.check.Checks

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
fun command(name: String, vararg aliases: String, commandCallback: DSLCommand.() -> Unit): DSLCommand {
    Check.all(name, "command", Checks.notBlank, Checks.noWhitespace)
    aliases.forEach { Check.all(it, "alias", Checks.notBlank, Checks.noWhitespace) }

    val dslCommand = DSLCommand(name, aliases.toList())
    dslCommand.apply(commandCallback)
    return dslCommand
}