package bvanseg.kotlincommons.lang.command.transformer.impl

import bvanseg.kotlincommons.graphic.Color
import bvanseg.kotlincommons.lang.command.token.buffer.ArgumentTokenBuffer
import bvanseg.kotlincommons.lang.command.transformer.Transformer

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
object ColorTransformer: Transformer<Color>(Color::class) {
    private val REGEX = Regex("^#[A-Fa-f0-9]{1,6}\$")
    override fun matches(buffer: ArgumentTokenBuffer): Boolean = buffer.peek()?.value?.matches(REGEX) ?: false
    override fun parse(buffer: ArgumentTokenBuffer): Color = Color(buffer.next().value.substringAfter("#").toInt(16))
}