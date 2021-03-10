package bvanseg.kotlincommons.lang.command.transformer.impl

import bvanseg.kotlincommons.grouping.enum.enumValueOfOrNull
import bvanseg.kotlincommons.lang.command.token.buffer.ArgumentTokenBuffer
import bvanseg.kotlincommons.lang.command.token.buffer.PeekingTokenBuffer
import bvanseg.kotlincommons.lang.command.transformer.EnumTransformer
import bvanseg.kotlincommons.time.api.KhronoUnit

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
object KhronoUnitTransformer: EnumTransformer<KhronoUnit>(KhronoUnit::class) {
    override fun matches(buffer: PeekingTokenBuffer): Boolean = enumValueOfOrNull<KhronoUnit>(buffer.peek()?.value ?: "", true) != null
    override fun parse(buffer: ArgumentTokenBuffer): KhronoUnit = enumValueOfOrNull<KhronoUnit>(buffer.next().value, true)!!
}