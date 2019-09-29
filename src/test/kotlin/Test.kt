import bvanseg.utilities.string.UTFBuffer

fun main() {
    val buffer = UTFBuffer.wrap("The fat cat jumped over the lazy dog.")

    println(buffer.next(5))
    println(buffer.nextWord())
    println(buffer.nextWord())
    println(buffer.nextWord())
    println(buffer.nextWord())
    println(buffer.nextWord())
    println(buffer.nextWord())
    println(buffer.nextWord())
    println(buffer.nextWord())
    println(buffer.nextWord())

    buffer.reset("The fat cat jumped over the lazy dog.\nDO YOU UNDERSTAND ME?")
    println(buffer.nextLine())
    println(buffer.nextChar())

    buffer.reset("You should ligma balls")
    println(buffer.getWordBefore("ligma"))
    println(buffer.getWordAfter("ligma"))

    buffer.reset("1 + (2(42 - (sqrt(x))^3) - 1)")

    while(buffer.getBetween("(", ")") != "") {
        val inner = buffer.getBetween("(", ")")
        buffer.reset(inner)
        println(inner)
    }

    buffer.reset("1 + (2(42 - (sqrt(x))^3) -1)")
    var original = buffer.nextNumber()?.toInt()
    while(original != null) {
        println(original)
        original = buffer.nextNumber()?.toInt()
    }
}