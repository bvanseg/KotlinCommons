package bvanseg.kotlincommons.lang.command.event

import bvanseg.kotlincommons.lang.command.CommandDispatcher
import bvanseg.kotlincommons.lang.command.context.CommandContext
import bvanseg.kotlincommons.lang.command.dsl.DSLCommand

/**
 * @author Boston Vanseghi
 * @since 2.10.2
 */
sealed class CommandEvent(val dispatcher: CommandDispatcher, var isCancelled: Boolean = false)

/**
 * @author Boston Vanseghi
 * @since 2.10.2
 */
sealed class CommandFireEvent(val command: DSLCommand, val context: CommandContext, dispatcher: CommandDispatcher) :
    CommandEvent(dispatcher) {
    class PRE(command: DSLCommand, context: CommandContext, dispatcher: CommandDispatcher) :
        CommandFireEvent(command, context, dispatcher)

    class POST(command: DSLCommand, context: CommandContext, dispatcher: CommandDispatcher) :
        CommandFireEvent(command, context, dispatcher)
}