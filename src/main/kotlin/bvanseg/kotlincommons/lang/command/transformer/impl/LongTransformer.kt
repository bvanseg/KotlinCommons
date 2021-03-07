package bvanseg.kotlincommons.lang.command.transformer.impl

import bvanseg.kotlincommons.lang.command.transformer.Transformer

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
object LongTransformer: Transformer<Long>(Long::class) {
    private val REGEX = Regex("^[+-]?\\d+\$")
    override fun matches(input: String): Boolean = input.matches(REGEX)
    override fun parse(input: String): Long = input.toLong()
}