package bvanseg.kotlincommons.collections

fun <E> List<E>.containsAny(list: List<E>): Boolean {
    list.forEach {
        if (this.contains(it))
            return true
    }
    return false
}

/**
 * Adds a vararg collection of [elements] to the [MutableCollection].
 *
 * @param elements - The elements to add to the collection.
 *
 * @return Whether or not adding all of the elements succeeded.
 *
 * @author Boston Vanseghi
 * @since 2.0.1
 */
fun <E> MutableCollection<E>.addAll(vararg elements: E): Boolean = this.addAll(*elements)