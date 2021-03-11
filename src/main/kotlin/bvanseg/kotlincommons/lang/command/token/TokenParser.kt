package bvanseg.kotlincommons.lang.command.token

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
class TokenParser internal constructor(private val input: String) {
    private var position: Int = 0

    fun next(): Char = input[position++]
    fun peek(): Char? = if (position < input.length) input[position] else null

    fun peekToken(): Token? = if (position < input.length) nextToken(true) else null

    fun nextToken(rewind: Boolean = false): Token {
        val currentPosition = position

        // Skip to start of next token.
        while (peek()?.isWhitespace() == true) next()

        val sb: StringBuilder = StringBuilder()

        var tokenType: TokenType = TokenType.SINGLE_STRING

        var isStartingCharacter = true
        while (position < input.length) {
            val next = next()
            when {
                next == '"' -> {
                    tokenType = TokenType.MULTI_STRING
                    while (peek() != '"' && peek() != null) { sb.append(next()) }
                    if (peek() == '"') next()
                }
                next == '-' && isStartingCharacter -> {
                    tokenType = if (peek() == '-') {
                        next() // Consume the extra dash token
                        TokenType.LONG_FLAG
                    } else TokenType.SHORT_FLAG
                    while (peek() != ' ' && peek() != null) { sb.append(next()) }
                }
                next == ' ' -> break
                else -> sb.append(next)
            }
            isStartingCharacter = false
        }

        if (rewind) {
            position = currentPosition
        }

        return Token(sb.toString(), tokenType)
    }

    fun getAllTokens(): List<Token> {
        val tokens = mutableListOf<Token>()
        while (peekToken() != null) { tokens.add(nextToken()) }
        return tokens
    }
}