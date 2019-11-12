package bvanseg.kotlincommons.ledger

import bvanseg.kotlincommons.classes.createNewInstance

/**
 * @author bright_spark
 * @author Boston Vanseghi
 * @since 2.1.0
 */
open class FactoryRegistry<K : Any, V : Any, FACTORY_ARG : Any?>(
    private val factory: (Class<out V>, FACTORY_ARG) -> V? = { klass, _ -> createNewInstance(klass) },
    keyExtractor: ((Class<out V>) -> K)? = null
) : GenericRegistry<K, Class<out V>>(keyExtractor) {
    open fun create(key: K, arg: FACTORY_ARG): V? = get(key)?.let { factory(it, arg) }
}