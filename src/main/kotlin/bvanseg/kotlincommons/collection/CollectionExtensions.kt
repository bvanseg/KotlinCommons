package bvanseg.kotlincommons.collection

/**
 * Checks if the given [Collection] object shares any elements with the other [collection] object.
 *
 * @return True if [this] contains any elements from within the other [collection], false otherwise.
 *
 * @author Boston Vanseghi
 */
fun <E> Collection<E>.containsAny(collection: Collection<E>): Boolean {
    collection.forEach {
        if (this.contains(it)) {
            return true
        }
    }
    return false
}