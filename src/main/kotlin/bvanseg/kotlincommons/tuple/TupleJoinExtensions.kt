package bvanseg.kotlincommons.tuple

/**
 * Allows the concatenation of a [Pair] to an object to produce a [Triple].
 *
 * @author Boston Vanseghi
 * @since 2.7.0
 */
infix fun <A, B, C> Pair<A, B>.to(that: C): Triple<A, B, C> = Triple(this.first, this.second, that)

/**
 * Allows the concatenation of a [Pair] to another [Pair] to produce a [Quad].
 *
 * @author Boston Vanseghi
 * @since 2.7.0
 */
infix fun <A, B, C, D> Pair<A, B>.to(that: Pair<C, D>): Quad<A, B, C, D> =
    Quad(this.first, this.second, that.first, that.second)

/**
 * Allows the concatenation of a [Triple] to an object to produce a [Quad].
 *
 * @author Boston Vanseghi
 * @since 2.7.0
 */
infix fun <A, B, C, D> Triple<A, B, C>.to(that: D): Quad<A, B, C, D> = Quad(this.first, this.second, this.third, that)