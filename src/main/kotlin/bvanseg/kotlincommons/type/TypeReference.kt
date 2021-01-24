package bvanseg.kotlincommons.type

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Inspired by the fasterXML implementation of this class.
 *
 * @author fasterXML Developers
 * @author Boston Vanseghi
 * @since 2.7.0
 */
class TypeReference<T> : Comparable<TypeReference<T>> {
    var type: Type

    override fun compareTo(other: TypeReference<T>): Int = 0

    init {
        val superClass = this.javaClass.genericSuperclass
        require(superClass !is Class<*>) { "Internal error: TypeReference constructed without actual type information" }
        type = (superClass as ParameterizedType).actualTypeArguments[0]
    }
}