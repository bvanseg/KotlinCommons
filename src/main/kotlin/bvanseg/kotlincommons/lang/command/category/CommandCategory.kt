package bvanseg.kotlincommons.lang.command.category

import bvanseg.kotlincommons.lang.command.CommandDispatcher

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
data class CommandCategory(val name: String, val path: String = CommandDispatcher.ROOT_CATEGORY.path) {
    override fun equals(other: Any?): Boolean = other is CommandCategory && other.path == path
    override fun hashCode(): Int = path.hashCode()
}