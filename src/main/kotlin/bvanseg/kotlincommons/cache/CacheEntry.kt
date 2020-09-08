package bvanseg.kotlincommons.cache

/**
 * @author Boston Vanseghi
 * @since 2.5.0
 */
data class CacheEntry<V>(val value: V, val expiration: Long)