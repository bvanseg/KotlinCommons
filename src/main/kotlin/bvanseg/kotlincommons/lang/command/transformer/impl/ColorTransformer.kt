package bvanseg.kotlincommons.lang.command.transformer.impl

import bvanseg.kotlincommons.graphic.Color
import bvanseg.kotlincommons.lang.command.transformer.Transformer

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
object ColorTransformer: Transformer<Color>(Color::class) {
    private val REGEX = Regex("^#[A-Fa-f0-9]{1,6}\$")
    override fun matches(input: String): Boolean = input.matches(REGEX)
    override fun parse(input: String): Color = Color(input.substringAfter("#").toInt(16))
}