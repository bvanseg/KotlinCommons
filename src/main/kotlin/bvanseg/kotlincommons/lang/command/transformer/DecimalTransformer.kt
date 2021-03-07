package bvanseg.kotlincommons.lang.command.transformer

import kotlin.reflect.KClass

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
abstract class DecimalTransformer internal constructor(type: KClass<out Number>): Transformer<Number>(type) {

    private val REGEX = Regex("^[+-]?([0-9]*[.])?[0-9]+$")

    override fun matches(input: String): Boolean = input.matches(REGEX) || input.equals("pi", true) || input.equals("e", true)

    override fun parse(input: String): Number = input
        .replace("pi", Math.PI.toString(), true)
        .replace("e", Math.E.toString(), true)
        .toDouble()
}