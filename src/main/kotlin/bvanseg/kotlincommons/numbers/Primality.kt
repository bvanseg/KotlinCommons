package bvanseg.kotlincommons.numbers

fun Number.isPrime(): Boolean {
    val value = this.toDouble()
    if(value < 2) return false

    var i = 2
    while(i * i <= value) {
        if (value % i == 0.0)
            return false

        if(i == 2)
            i++
        else
            i += 2
    }
    return true
}