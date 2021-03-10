package bvanseg.kotlincommons.lang.command.token.buffer

import bvanseg.kotlincommons.lang.command.token.Token
import java.util.LinkedList

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
open class TokenBuffer(tokens: List<Token>): PeekingTokenBuffer {
    private val tokens: LinkedList<Token> = LinkedList(tokens)

    fun isNotEmpty(): Boolean = tokens.isNotEmpty()

    val tokenCount: Int
        get() = tokens.size

    override fun peek(): Token? = tokens.firstOrNull()
    override fun peek(n: Int): List<Token> = tokens.subList(0, n)
    override fun peekAll(): List<Token> = tokens
    fun next(): Token = tokens.removeFirst()
}