package bvanseg.kotlincommons.lang.command.transformer.impl.time

import bvanseg.kotlincommons.lang.command.transformer.EnumTransformer
import bvanseg.kotlincommons.time.api.KhronoUnit

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
object KhronoUnitTransformer: EnumTransformer<KhronoUnit>(KhronoUnit::class)