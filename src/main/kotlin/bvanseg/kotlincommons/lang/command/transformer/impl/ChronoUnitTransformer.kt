package bvanseg.kotlincommons.lang.command.transformer.impl

import bvanseg.kotlincommons.grouping.enum.enumValueOfOrNull
import bvanseg.kotlincommons.lang.command.token.buffer.ArgumentTokenBuffer
import bvanseg.kotlincommons.lang.command.transformer.EnumTransformer
import java.time.temporal.ChronoUnit

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
object ChronoUnitTransformer: EnumTransformer<ChronoUnit>(ChronoUnit::class) {
    override fun matches(buffer: ArgumentTokenBuffer): Boolean = enumValueOfOrNull<ChronoUnit>(buffer.peek()?.value ?: "", true) != null
    override fun parse(buffer: ArgumentTokenBuffer): ChronoUnit = enumValueOfOrNull<ChronoUnit>(buffer.next().value, true)!!
}