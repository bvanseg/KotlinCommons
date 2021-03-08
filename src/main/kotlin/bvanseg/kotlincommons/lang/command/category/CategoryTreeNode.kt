package bvanseg.kotlincommons.lang.command.category

import bvanseg.kotlincommons.lang.command.dsl.DSLCommand

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
data class CategoryTreeNode(
    val category: String,
    val subcategories: MutableList<CategoryTreeNode> = mutableListOf(),
    val commands: MutableList<DSLCommand> = mutableListOf()
) {


    fun populateChain(category: SimpleCategoryNode) {
        var currentTree: CategoryTreeNode = this
        var current: SimpleCategoryNode? = category
        while (current != null) {
            var nextTreeNode = currentTree.subcategories.find { it.category == current!!.category }

            if (nextTreeNode == null) {
                nextTreeNode = CategoryTreeNode(current.category)
                currentTree.subcategories.add(nextTreeNode)
            }

            currentTree = nextTreeNode
            current = current.next
        }
    }

    override fun equals(other: Any?): Boolean = other is CategoryTreeNode && this.category == other.category
    override fun hashCode(): Int = category.hashCode()
}