package bvanseg.kotlincommons.lang.command.transformer.impl

import bvanseg.kotlincommons.grouping.enum.enumValueOfOrNull
import bvanseg.kotlincommons.lang.command.transformer.EnumTransformer
import bvanseg.kotlincommons.time.api.KhronoUnit
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
object ChronoUnitTransformer: EnumTransformer<ChronoUnit>(ChronoUnit::class) {
    override fun matches(input: String): Boolean = enumValueOfOrNull<ChronoUnit>(input, true) != null
    override fun parse(input: String): ChronoUnit = enumValueOfOrNull<ChronoUnit>(input, true)!!
}