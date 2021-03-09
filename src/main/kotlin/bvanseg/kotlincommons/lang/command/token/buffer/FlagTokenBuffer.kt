package bvanseg.kotlincommons.lang.command.token.buffer

import bvanseg.kotlincommons.lang.command.token.SubTokenType
import bvanseg.kotlincommons.lang.command.token.Token

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
class FlagTokenBuffer(tokens: List<Token>): TokenBuffer(tokens.filter { it.tokenType.subTokenType == SubTokenType.FLAG })