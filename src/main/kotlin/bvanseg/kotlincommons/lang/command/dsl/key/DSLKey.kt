package bvanseg.kotlincommons.lang.command.dsl.key

import kotlin.reflect.KClass

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
abstract class DSLKey<T: Any>(val name: String, val type: KClass<T>)