package bvanseg.kotlincommons.lang.command.transformer.impl.time

import bvanseg.kotlincommons.lang.command.transformer.EnumTransformer
import java.time.DayOfWeek

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
object DayOfWeekTransformer: EnumTransformer<DayOfWeek>(DayOfWeek::class)