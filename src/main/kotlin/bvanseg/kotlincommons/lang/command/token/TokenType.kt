package bvanseg.kotlincommons.lang.command.token

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
enum class TokenType(val subTokenType: SubTokenType) {
    LONG_FLAG(SubTokenType.FLAG),
    MULTI_STRING(SubTokenType.ARGUMENT),
    SINGLE_STRING(SubTokenType.ARGUMENT),
    SHORT_FLAG(SubTokenType.FLAG)
}