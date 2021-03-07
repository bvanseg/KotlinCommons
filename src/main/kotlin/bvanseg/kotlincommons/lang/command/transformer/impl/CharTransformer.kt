package bvanseg.kotlincommons.lang.command.transformer.impl

import bvanseg.kotlincommons.lang.command.transformer.Transformer

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
object CharTransformer: Transformer<Char>(Char::class) {
    private val REGEX = Regex("^.$")
    override fun matches(input: String): Boolean = input.matches(REGEX)
    override fun parse(input: String): Char = input[0]
}