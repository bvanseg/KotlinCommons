package bvanseg.kotlincommons.net

/**
 * @author Boston Vanseghi
 * @since 2.5.1
 */
object QueryHandler {

    fun build(vararg pairs: Pair<String, Any?>) =
            "?" + pairs.filter { it.second != null }.joinToString("&") { pair -> "${pair.first}=${pair.second}" }

    fun derive(query: String): List<Pair<String, String>> = query.replace("?", "").split('&').run {
        val pairs = mutableListOf<Pair<String, String>>()

        for (param in this) {
            val split = param.split("=")
            pairs.add(split[0] to split[1])
        }

        return pairs
    }
}