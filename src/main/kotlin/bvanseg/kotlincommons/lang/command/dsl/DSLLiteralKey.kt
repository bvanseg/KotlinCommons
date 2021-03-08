package bvanseg.kotlincommons.lang.command.dsl

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
class DSLLiteralKey(val literal: DSLCommandLiteral, name: String): DSLKey<String>(name, String::class)