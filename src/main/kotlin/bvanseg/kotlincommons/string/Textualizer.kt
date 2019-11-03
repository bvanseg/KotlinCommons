package bvanseg.kotlincommons.string

import java.lang.StringBuilder

class Textualizer private constructor() {

    private val data = StringBuilder()

    private var elements: Int = 0

    fun append(string: String): Textualizer = this.apply { data.append(string) }

    fun append(field: String, value: Any): Textualizer = this.apply {
        val con = when(value::class) {
            Byte::class, Short::class,
            Int::class, Long::class,
            Float::class, Double::class,
            String::class -> value.toString()
            else -> {
                if(value::class.java.isEnum)
                    value::class.java.enumConstants[(value as Enum<*>).ordinal]
                else
                    value::class.qualifiedName
            }
        }

        if(elements == 0)
            data.append("$field=$con")
        else
            data.append(", $field=$con")

        elements++
    }

    override fun toString(): String = "$data]"

    companion object {
        @JvmStatic
        fun builder(): Textualizer = Textualizer().apply {
            this.append("[")
        }
    }
}