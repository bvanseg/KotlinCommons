package bvanseg.kotlincommons.reflect

import kotlin.reflect.full.createInstance

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
fun <T : Any> Class<T>.createInstance(): T = this.kotlin.createInstance()