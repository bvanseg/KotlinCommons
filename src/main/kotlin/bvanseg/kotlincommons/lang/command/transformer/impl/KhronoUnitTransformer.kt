package bvanseg.kotlincommons.lang.command.transformer.impl

import bvanseg.kotlincommons.grouping.enum.enumValueOfOrNull
import bvanseg.kotlincommons.lang.command.transformer.EnumTransformer
import bvanseg.kotlincommons.time.api.KhronoUnit

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
object KhronoUnitTransformer: EnumTransformer<KhronoUnit>(KhronoUnit::class) {
    override fun matches(input: String): Boolean = enumValueOfOrNull<KhronoUnit>(input, true) != null
    override fun parse(input: String): KhronoUnit = enumValueOfOrNull<KhronoUnit>(input, true)!!
}