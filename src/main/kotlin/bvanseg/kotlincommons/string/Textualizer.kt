package bvanseg.kotlincommons.string

import java.lang.StringBuilder

class Textualizer private constructor() {

    private val data = StringBuilder()

    private var elements: Int = 0

    fun append(string: String): Textualizer = this.apply { data.append(string) }

    @Suppress("IMPLICIT_CAST_TO_ANY")
    fun <T: Any> append(field: String, value: T): Textualizer = this.apply {
        val con = when(value::class) {
            String::class -> "\"${value as String}\""
            Map::class -> (value as Map<*, *>).entries.joinToString(", ")
            else -> {
                when {
                    value::class.java.isEnum -> value::class.java.enumConstants[(value as Enum<*>).ordinal]
                    value::class.java.isArray -> "[${(value as Array<Any>).joinToString(", ")}]"
                    else -> value.toString()
                }
            }
        }

        if(elements == 0)
            data.append("$field=$con")
        else
            data.append(", $field=$con")

        elements++
    }

    override fun toString(): String = "$data)"

    companion object {
        @JvmStatic
        fun builder(clazz: Any): Textualizer = Textualizer().apply {
            this.append("${clazz::class.simpleName}(")
        }
    }
}