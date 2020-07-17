package bvanseg.kotlincommons.armada.tree

import bvanseg.kotlincommons.armada.contexts.Context
import bvanseg.kotlincommons.armada.contexts.EmptyContext
import bvanseg.kotlincommons.collections.BinaryTree
import bvanseg.kotlincommons.collections.TreeNode

interface DSLContext
object EmptyDSLContext: DSLContext
interface CommandNode

/**
 * Root of the command dsl. Never a leaf.
 */
class CommandRootNode(val name: String): CommandNode

/**
 * Executor node. ALWAYS a leaf.
 */
class CommandExecutorNode: CommandNode

/**
 * Parameter node. NEVER a leaf and NEVER a root.
 */
class CommandParameterNode(val name: String): CommandNode

/**
 * Base of the command dsl. Stores the command name so far. The functions inside can be turned into DSL blocks.
 *
 * Contains a tree. THIS IS VERY IMPORTANT. The tree is meant to path the possible routes the command can take.
 *
 * When the tree encounters executor nodes, it knows for a fact the command ends there. There should be no other nodes
 * as children to an executor node.
 */
class CommandDSL(val name: String) {

    var tree = BinaryTree<CommandNode>(TreeNode(CommandRootNode(name), null, null))

    /**
     * How do we store that execution block?
     * How do we know where we are currently in the tree? (Perhaps a DSLContext could be necessary?)
     */
    fun <T> parameter(paramName: String, block: CommandParameterNode.(T) -> Unit): CommandParameterNode {
        val parameter = CommandParameterNode(paramName)
        return parameter
    }

    /**
     * Executor block. Has a generic arg for returning from the executor. This would need to loop back to the execute
     * function from which the DSL started firing from and return [R] to that.
     */
    fun <R> executor(block: CommandExecutorNode.() -> R): CommandExecutorNode {
        val executor = CommandExecutorNode()
        executor.block()
        return executor
    }
}

/**
 * This is the starting point of making a command. As you can see, the context (type-bound) is passed as a
 * generic argument to the function. Below in [foo], it's accessible to the entire dsl builder pattern.
 */
fun <T: Context> command(name: String, block: CommandDSL.(T) -> Unit): CommandDSL {
    val commandDSL = CommandDSL(name)
    commandDSL.block(EmptyContext as T)
    return commandDSL
}

fun foo() {
    command<Context>("foo") { context ->
        parameter<Int>("bar") { bar ->
            executor {

                return@executor bar
            }
        }
        executor {

            return@executor 0
        }
    }
}