package bvanseg.kotlincommons.lang.command.transformer.impl.time

import bvanseg.kotlincommons.lang.command.transformer.EnumTransformer
import java.util.concurrent.TimeUnit

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
object TimeUnitTransformer: EnumTransformer<TimeUnit>(TimeUnit::class)