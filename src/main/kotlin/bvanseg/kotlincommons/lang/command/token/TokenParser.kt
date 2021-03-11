/*
 * MIT License
 *
 * Copyright (c) 2021 Boston Vanseghi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package bvanseg.kotlincommons.lang.command.token

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
class TokenParser internal constructor(private val input: String) {
    private val DECIMAL_REGEX = Regex("^[+-]?([0-9]*[.])?[0-9]+$")
    private val INTEGER_REGEX = Regex("^[+-]?\\d+\$")

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
                    while (peek() != '"' && peek() != null) {
                        sb.append(next())
                    }
                    if (peek() == '"') next()
                }
                next == '-' && isStartingCharacter -> {
                    tokenType = if (peek() == '-') {
                        next() // Consume the extra dash token
                        TokenType.LONG_FLAG
                    } else TokenType.SHORT_FLAG
                    while (peek() != ' ' && peek() != null) {
                        sb.append(next())
                    }
                    // Avoid marking token as a flag if it resembles a number.
                    if (sb.matches(INTEGER_REGEX) || sb.matches(DECIMAL_REGEX)) {
                        sb.insert(0, next) // Re-insert the dash used that triggered flag processing.
                        tokenType = TokenType.SINGLE_STRING // Reset back to non-flag status.
                    }
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
        while (peekToken() != null) {
            tokens.add(nextToken())
        }
        return tokens
    }
}