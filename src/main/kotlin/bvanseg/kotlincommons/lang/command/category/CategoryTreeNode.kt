package bvanseg.kotlincommons.lang.command.category

import bvanseg.kotlincommons.lang.command.CommandProperties
import bvanseg.kotlincommons.lang.command.dsl.DSLCommand
import bvanseg.kotlincommons.lang.string.ToStringBuilder

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
data class CategoryTreeNode(
    val parent: CategoryTreeNode?,
    val category: String,
    val subcategories: MutableList<CategoryTreeNode> = mutableListOf()
) {

    val commands: MutableList<DSLCommand<out CommandProperties>> = mutableListOf()

    val fullPath: String by lazy {
        val list = mutableListOf<String>()
        list.add(category)
        var currentParent = parent
        while (currentParent != null) {
            list.add(currentParent.category)
            currentParent = currentParent.parent
        }
        return@lazy list.reversed().filter { it.isNotBlank() }.joinToString(".")
    }

    fun populateChain(category: SimpleCategoryNode) {
        var currentTree: CategoryTreeNode = this
        var current: SimpleCategoryNode? = category
        while (current != null) {
            var nextTreeNode = currentTree.subcategories.find { it.category == current!!.category }

            if (nextTreeNode == null) {
                nextTreeNode = CategoryTreeNode(currentTree, current.category)
                currentTree.subcategories.add(nextTreeNode)
            }

            currentTree = nextTreeNode
            current = current.next
        }
    }

    fun getFullPathCategories(): List<CategoryTreeNode> {
        if (subcategories.isEmpty()) {
            return listOf(this)
        }

        val leaves = mutableListOf<CategoryTreeNode>()

        for (subcategory in subcategories) {
            leaves += subcategory.getFullPathCategories()
        }

        return leaves
    }

    override fun equals(other: Any?): Boolean = other is CategoryTreeNode && this.category == other.category

    override fun hashCode(): Int = category.hashCode()

    override fun toString(): String = ToStringBuilder.builder(this::class)
        .append("parent", parent?.category) // Simplify parent string to avoid stack overflow error.
        .append("category", category)
        .append("subcategories", subcategories)
        .toString()
}