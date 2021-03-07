package bvanseg.kotlincommons.lang.command.transformer.impl

import bvanseg.kotlincommons.grouping.enum.enumValueOfOrNull
import bvanseg.kotlincommons.lang.command.transformer.EnumTransformer
import bvanseg.kotlincommons.time.api.KhronoUnit
import java.util.concurrent.TimeUnit

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
object TimeUnitTransformer: EnumTransformer<TimeUnit>(TimeUnit::class) {
    override fun matches(input: String): Boolean = enumValueOfOrNull<TimeUnit>(input, true) != null
    override fun parse(input: String): TimeUnit = enumValueOfOrNull<TimeUnit>(input, true)!!
}