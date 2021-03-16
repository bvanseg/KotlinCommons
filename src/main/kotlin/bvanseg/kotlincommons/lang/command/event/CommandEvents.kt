package bvanseg.kotlincommons.lang.command.event

import bvanseg.kotlincommons.lang.command.CommandDispatcher
import bvanseg.kotlincommons.lang.command.dsl.DSLCommand

/**
 * @author Boston Vanseghi
 * @since 2.10.2
 */
sealed class CommandEvent(val dispatcher: CommandDispatcher)

/**
 * @author Boston Vanseghi
 * @since 2.10.2
 */
sealed class CommandFireEvent(val command: DSLCommand, dispatcher: CommandDispatcher) : CommandEvent(dispatcher) {
    class PRE(command: DSLCommand, dispatcher: CommandDispatcher) : CommandFireEvent(command, dispatcher)
    class POST(command: DSLCommand, dispatcher: CommandDispatcher) : CommandFireEvent(command, dispatcher)
}