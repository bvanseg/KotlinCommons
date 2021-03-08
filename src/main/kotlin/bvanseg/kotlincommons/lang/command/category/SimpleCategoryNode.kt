package bvanseg.kotlincommons.lang.command.category

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
data class SimpleCategoryNode(val category: String, var next: SimpleCategoryNode? = null)