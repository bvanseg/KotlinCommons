package bvanseg.kotlincommons.lang.command.transformer

import bvanseg.kotlincommons.lang.command.token.buffer.ArgumentTokenBuffer
import bvanseg.kotlincommons.lang.command.token.buffer.PeekingTokenBuffer
import kotlin.reflect.KClass

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
abstract class DecimalTransformer internal constructor(type: KClass<out Number>): Transformer<Number>(type) {

    private val REGEX = Regex("^[+-]?([0-9]*[.])?[0-9]+$")

    override fun matches(buffer: PeekingTokenBuffer): Boolean {
        val token = buffer.peek() ?: return false
        val input = token.value
        return input.matches(REGEX) || input.equals("pi", true) || input.equals("e", true)
    }

    override fun parse(buffer: ArgumentTokenBuffer): Number = buffer.next().value.replace("pi", Math.PI.toString(), true)
        .replace("e", Math.E.toString(), true)
        .toDouble()
}