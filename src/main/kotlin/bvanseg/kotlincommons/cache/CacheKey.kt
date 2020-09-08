package bvanseg.kotlincommons.cache

/**
 * @author Boston Vanseghi
 * @since 2.5.0
 */
class CacheKey<T>(val value: T, var timeToLiveMS: Long) {

    override fun equals(other: Any?): Boolean {
        if(other is CacheKey<*>) {
            return value != null && value == other.value
        }

        return false
    }

    override fun hashCode(): Int = value.hashCode()
}