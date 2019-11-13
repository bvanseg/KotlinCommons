package bvanseg.kotlincommons.numbers

/**
 * Hashes
 */
class Hasher {

    var total = 17

    private val mult = 37

    fun append(value: Any?): Hasher {
        if(value == null)
            total *= mult
        else {
            if(value.javaClass.isArray) {
                @Suppress("UNCHECKED_CAST")
                append(value as Array<Any?>)
            }else
                total = total * mult + value.hashCode()
        }
        return this
    }

    fun append(array: Array<Any?>): Hasher {
        array.forEach {
            total = total * mult + it.hashCode()
        }
        return this
    }

    override fun hashCode(): Int = total

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Hasher
        if (total != other.total) return false
        return true
    }

    companion object {
        @JvmStatic
        fun builder(clazz: Any): Hasher = Hasher().apply {
            this.append(clazz::class.simpleName)
        }
    }
}