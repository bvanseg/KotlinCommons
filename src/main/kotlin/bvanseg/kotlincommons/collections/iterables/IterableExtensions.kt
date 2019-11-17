package bvanseg.kotlincommons.collections.iterables

inline fun <T> Iterable<T>.sumByByte(selector: (T) -> Byte): Byte {
    var sum = 0
    for (element in this) {
        sum += selector(element)
    }
    return sum.toByte()
}

inline fun <T> Iterable<T>.sumByShort(selector: (T) -> Short): Short {
    var sum = 0L
    for (element in this) {
        sum += selector(element)
    }
    return sum.toShort()
}

inline fun <T> Iterable<T>.sumByLong(selector: (T) -> Long): Long {
    var sum = 0L
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

inline fun <T> Iterable<T>.sumByFloat(selector: (T) -> Float): Float {
    var sum = 0f
    for (element in this) {
        sum += selector(element)
    }
    return sum
}