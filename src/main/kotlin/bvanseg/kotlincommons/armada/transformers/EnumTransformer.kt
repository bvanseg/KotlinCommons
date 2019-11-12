package bvanseg.kotlincommons.armada.transformers

import bvanseg.kotlincommons.armada.contexts.Context
import kotlin.reflect.KClass

/**
 * A base enum transformer implementation that should be extended for specific enums
 * @see TimeUnitTransformer
 *
 * @author bright_spark
 * @since 2.1.0
 */
open class EnumTransformer<T : Enum<*>>(type: KClass<T>) : Transformer<T>(type) {
    private val values = type.java.enumConstants

    override fun parse(input: String, ctx: Context?): T? =
        input.trim().toUpperCase().let { time -> values.find { it.name == time } }
}
