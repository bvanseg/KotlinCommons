package bvanseg.kotlincommons.lang.command.dsl

import bvanseg.kotlincommons.lang.command.context.CommandContext

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
class DSLCommandExecutor<T>(val parent: DSLCommandNode, val block: (CommandContext) -> T): DSLCommandNode()