package bvanseg.kotlincommons.collections

import java.util.*

/**
 * A very minimal class that limits the number of elements that can be added to it.
 *
 * @author bright_spark
 * @since 1.0.1
 */
class SizedList<T>(private val maxSize: Int) : LinkedList<T>() {

    /**
     * Adds an element to the list. If the current size of the list exceeds the max size, the head element of the list
     * will be dropped.
     */
    override fun add(element: T): Boolean {
        if (this.size == maxSize)
            this.removeAt(0)
        return super.add(element)
    }
}