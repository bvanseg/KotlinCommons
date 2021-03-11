package bvanseg.kotlincommons.lang.command.transformer.impl.time

import bvanseg.kotlincommons.lang.command.transformer.EnumTransformer
import java.time.Month

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
object MonthTransformer: EnumTransformer<Month>(Month::class)