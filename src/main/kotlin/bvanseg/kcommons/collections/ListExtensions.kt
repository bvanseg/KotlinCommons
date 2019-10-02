package bvanseg.kcommons.collections

fun <E> List<E>.containsAny(list: List<E>): Boolean {
    list.forEach {
        if(this.contains(it)) {
            return true
        }
    }
    return false
}