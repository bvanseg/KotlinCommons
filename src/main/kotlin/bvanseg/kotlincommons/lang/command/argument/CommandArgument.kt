package bvanseg.kotlincommons.lang.command.argument

import kotlin.reflect.KClass

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
class CommandArgument<T: Any>(val value: T, val type: KClass<*>)