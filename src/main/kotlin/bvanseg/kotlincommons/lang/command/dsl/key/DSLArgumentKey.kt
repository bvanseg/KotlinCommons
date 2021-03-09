package bvanseg.kotlincommons.lang.command.dsl.key

import bvanseg.kotlincommons.lang.command.dsl.node.DSLCommandArgument
import kotlin.reflect.KClass

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
class DSLArgumentKey<T: Any>(val argument: DSLCommandArgument<T>, name: String, type: KClass<T>): DSLKey<T>(name, type)