package bvanseg.kotlincommons.lang.command.transformer.impl

import bvanseg.kotlincommons.lang.command.transformer.Transformer

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
object BooleanTransformer: Transformer<Boolean>(Boolean::class) {
    private val REGEX = Regex("^true|false$", RegexOption.IGNORE_CASE)
    override fun matches(input: String): Boolean = input.matches(REGEX)
    override fun parse(input: String): Boolean = input.toBoolean()
}