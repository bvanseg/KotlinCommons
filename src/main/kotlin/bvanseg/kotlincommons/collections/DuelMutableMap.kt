package bvanseg.kotlincommons.collections

/**
 * A simple [HashMap] implementation that makes usage of [ReverseMutableMap] in order to create a backwards-linkable
 * map, where values on the [LEFT] can be used to fetch values on the [RIGHT] and vice-versa.
 *
 * @author Boston Vanseghi
 * @since 2.0.2
 */
class DuelMutableMap<LEFT, RIGHT>: HashMap<LEFT, RIGHT>() {

    private val reverse = ReverseMutableMap(this)

    fun getLeft(right: RIGHT): LEFT? = reverse[right]
    fun getRight(left: LEFT): RIGHT? = this[left]

    fun putLeft(right: RIGHT, left: LEFT): LEFT? = reverse.put(right, left)
    fun putRight(left: LEFT, right: RIGHT): RIGHT? = this.put(left, right)
}