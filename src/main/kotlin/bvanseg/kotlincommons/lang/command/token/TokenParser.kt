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

import bvanseg.kotlincommons.io.logging.getLogger
import bvanseg.kotlincommons.io.logging.trace

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
class TokenParser internal constructor(private val input: String) {

    companion object {
        private val logger = getLogger()
    }

    private var position: Int = 0

    fun next(): Char = input[position++]
    fun peek(): Char? = if (position < input.length) input[position] else null
    fun peekAt(n: Int): Char? = if (position + n < input.length) input[position + n] else null
    fun peek(n: Int): String? = if (position + n < input.length) input.substring(position, position + n) else null

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
                    logger.trace { "[${if(rewind) "PEEK" else "NEXT"}] Attempting to tokenize potential multi-string argument." }
                    tokenType = TokenType.MULTI_STRING
                    while (peek(2) != "\" " && peek(2) != null) {
                        sb.append(next())
                    }
                    if (peek() == '"') next()
                }
                next == '-' && isStartingCharacter -> {
                    logger.trace { "[${if(rewind) "PEEK" else "NEXT"}] Attempting to tokenize potential command flag." }
                    val potentialLiteralBuilder = StringBuilder().append(next)

                    if (peek() == '-') {
                        potentialLiteralBuilder.append(next())
                        tokenType = TokenType.LONG_FLAG
                        if (peek()?.isLetter() != true) {
                            logger.trace { "[${if(rewind) "PEEK" else "NEXT"}] Failed to parse long flag out of '$potentialLiteralBuilder'." }
                            sb.clear()
                            sb.append(potentialLiteralBuilder.toString())
                            tokenType = TokenType.SINGLE_STRING
                            break
                        }
                        while (peek()?.isLetter() == true) {
                            val n = next()
                            potentialLiteralBuilder.append(n)
                            sb.append(n)
                        }
                        if (peek()?.isWhitespace() == false) {
                            // Some non-letter interrupted flag parsing, treat as literal/argument.
                            logger.trace { "[${if(rewind) "PEEK" else "NEXT"}] Failed to parse long flag out of '$potentialLiteralBuilder'." }
                            sb.clear()
                            sb.append(potentialLiteralBuilder.toString())
                            tokenType = TokenType.SINGLE_STRING
                            break
                        }
                    } else {
                        tokenType = TokenType.SHORT_FLAG
                        if (peek()?.isLetter() != true) {
                            logger.trace { "[${if(rewind) "PEEK" else "NEXT"}] Failed to parse short flag out of '$potentialLiteralBuilder'." }
                            sb.clear()
                            sb.append(potentialLiteralBuilder.toString())
                            tokenType = TokenType.SINGLE_STRING
                            break
                        }
                        while (peek()?.isLetter() == true) {
                            val n = next()
                            potentialLiteralBuilder.append(n)
                            sb.append(n)
                        }
                        if (peek()?.isWhitespace() == false) {
                            // Some non-letter interrupted flag parsing, treat as literal/argument.
                            logger.trace { "[${if(rewind) "PEEK" else "NEXT"}] Failed to parse short flag out of '$potentialLiteralBuilder'." }
                            sb.clear()
                            sb.append(potentialLiteralBuilder.toString())
                            tokenType = TokenType.SINGLE_STRING
                            break
                        }
                    }
                    logger.trace { "[${if(rewind) "PEEK" else "NEXT"}] Successfully parsed flag out of '$sb'." }
                    break
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