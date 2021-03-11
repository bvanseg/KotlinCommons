package bvanseg.kotlincommons.lang.command.dsl.node

import bvanseg.kotlincommons.lang.command.context.CommandContext

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
class DSLCommandExceptionCatcher<T>(val parent: DSLCommandNode, val block: (CommandContext, Throwable) -> T): DSLCommandNode()