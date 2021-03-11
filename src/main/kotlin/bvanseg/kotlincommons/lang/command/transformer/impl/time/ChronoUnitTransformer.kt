package bvanseg.kotlincommons.lang.command.transformer.impl.time

import bvanseg.kotlincommons.lang.command.transformer.EnumTransformer
import java.time.temporal.ChronoUnit

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
object ChronoUnitTransformer: EnumTransformer<ChronoUnit>(ChronoUnit::class)