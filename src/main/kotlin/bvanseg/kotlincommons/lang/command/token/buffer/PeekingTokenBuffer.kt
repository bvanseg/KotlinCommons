package bvanseg.kotlincommons.lang.command.token.buffer

import bvanseg.kotlincommons.lang.command.token.Token

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
interface PeekingTokenBuffer {
    fun peek(): Token?
    fun peek(n: Int): List<Token>
    fun peekAll(): List<Token>
}