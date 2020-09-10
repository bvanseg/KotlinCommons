package bvanseg.kotlincommons.enum

import java.util.EnumSet

/**
 *
 * @author Boston Vanseghi
 * @since 2.5.0
 */
inline fun <reified E: Enum<E>> enumSetOf(vararg values: E): EnumSet<E> = when {
    values.isEmpty() -> EnumSet.noneOf(E::class.java)
    values.size == 1 -> EnumSet.of(values.first())
    else -> EnumSet.of(values.first(), *values.slice(1 until values.size).toTypedArray())
}