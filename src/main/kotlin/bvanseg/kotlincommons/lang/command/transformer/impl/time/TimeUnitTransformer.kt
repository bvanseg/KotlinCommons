package bvanseg.kotlincommons.lang.command.transformer.impl.time

import bvanseg.kotlincommons.grouping.enum.enumValueOfOrNull
import bvanseg.kotlincommons.lang.command.token.buffer.ArgumentTokenBuffer
import bvanseg.kotlincommons.lang.command.token.buffer.PeekingTokenBuffer
import bvanseg.kotlincommons.lang.command.transformer.EnumTransformer
import java.util.concurrent.TimeUnit

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
object TimeUnitTransformer: EnumTransformer<TimeUnit>(TimeUnit::class) {
    override fun matches(buffer: PeekingTokenBuffer): Boolean = enumValueOfOrNull<TimeUnit>(buffer.peek()?.value ?: "", true) != null
    override fun parse(buffer: ArgumentTokenBuffer): TimeUnit = enumValueOfOrNull<TimeUnit>(buffer.next().value, true)!!
}