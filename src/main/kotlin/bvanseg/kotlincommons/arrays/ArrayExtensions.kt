package bvanseg.kotlincommons.arrays

import java.util.*

/**
 * Gets a random element from the [Array].
 *
 * @author Boston Vanseghi
 * @since 2.0.2
 */
fun <T> Array<T>.random(rnd: Random = Random()): T = get(rnd.nextInt(size))

/**
 * Gets a random element from the 2D [Array].
 *
 * @author Boston Vanseghi
 * @since 2.0.2
 */
fun <T> Array<Array<T>>.random(rnd: Random = Random()): T = get(rnd.nextInt(size))[rnd.nextInt(size)]

/**
 * Gets a random element from the 3D [Array].
 *
 * @author Boston Vanseghi
 * @since 2.0.2
 */
fun <T> Array<Array<Array<T>>>.random(rnd: Random = Random()): T = get(rnd.nextInt(size))[rnd.nextInt(size)][rnd.nextInt(size)]

/**
 * Checks the [Array] to see if any of the elements are null.
 *
 * @author Boston Vanseghi
 * @since 2.0.2
 */
inline fun <reified T> Array<T>.anyNull(): Boolean = any { it == null }

/**
 * Checks the 2D [Array] to see if any of the elements are null.
 *
 * @author Boston Vanseghi
 * @since 2.0.2
 */
inline fun <reified T> Array<Array<T>>.anyNull(): Boolean = any { e1 -> e1.any { e2 -> e2 == null } }

/**
 * Checks the 3D [Array] to see if any of the elements are null.
 *
 * @author Boston Vanseghi
 * @since 2.0.2
 */
inline fun <reified T> Array<Array<Array<T>>>.anyNull(): Boolean = any { e1 -> e1.any { e2 -> e2.any { e3 -> e3 == null } } }