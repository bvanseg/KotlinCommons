package bvanseg.kotlincommons.lang.command.transformer

import kotlin.reflect.KClass

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
abstract class EnumTransformer<E : Enum<E>>(type: KClass<E>): Transformer<E>(type)