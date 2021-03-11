package bvanseg.kotlincommons.grouping.enum

import kotlin.reflect.KClass

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
fun <T: Enum<T>> KClass<out T>.enumValueOfOrNull(name: String?, ignoreCase: Boolean = false): T? =
    if (name == null) null else this.java.enumConstants.find { it.name.equals(name, ignoreCase) }

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
fun <T: Enum<T>> KClass<out T>.getOrNull(index: Int): T? = this.java.enumConstants.let {
    if (index >= 0 && index < it.size) {
        it[index]
    } else null
}