package bvanseg.kotlincommons.lang.command.token

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
class TokenParser internal constructor(private val input: String) {
    private var position: Int = 0

    fun next(): Char = input[position++]
    fun peek(): Char? = if (position < input.length) input[position] else null

    fun peekToken(): String? = if (position < input.length) nextToken(true) else null

    fun nextToken(rewind: Boolean = false): String {
        val currentPosition = position

        // Skip to start of next token.
        while (peek()?.isWhitespace() == true) next()

        val sb: StringBuilder = StringBuilder()

        while (position < input.length) {
            when (val next = next()) {
                '"' -> while (peek() != '"' && peek() != null) { sb.append(next()) }
                ' ' -> break
                else -> sb.append(next)
            }
        }

        if (rewind) {
            position = currentPosition
        }

        return sb.toString()
    }

    fun getAllTokens(): List<String> {
        val tokens = mutableListOf<String>()
        while (peekToken() != null) { tokens.add(nextToken()) }
        return tokens
    }
}