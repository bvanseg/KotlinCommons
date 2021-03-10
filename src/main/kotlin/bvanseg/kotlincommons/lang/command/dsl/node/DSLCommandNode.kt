package bvanseg.kotlincommons.lang.command.dsl.node

import bvanseg.kotlincommons.lang.check.Check
import bvanseg.kotlincommons.lang.check.Checks
import bvanseg.kotlincommons.lang.command.exception.DuplicateArgumentTypeException
import bvanseg.kotlincommons.lang.command.exception.DuplicateExecutorException
import bvanseg.kotlincommons.lang.command.exception.DuplicateLiteralException
import bvanseg.kotlincommons.lang.command.context.CommandContext
import bvanseg.kotlincommons.lang.command.dsl.key.DSLArgumentKey
import bvanseg.kotlincommons.lang.command.dsl.key.DSLLiteralKey

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
abstract class DSLCommandNode {
    var description: String = ""

    val literals: MutableList<DSLCommandLiteral> = mutableListOf()
    val arguments: MutableList<DSLCommandArgument<*>> = mutableListOf()
    var executor: DSLCommandExecutor<*>? = null

    fun literal(literalValue: String, vararg extraLiteralValues: String, block: DSLCommandLiteral.(DSLLiteralKey) -> Unit) {
        Check.all(literalValue, "literal", Checks.notBlank, Checks.noWhitespace)
        extraLiteralValues.forEach { Check.all(it, "literal", Checks.notBlank, Checks.noWhitespace) }

        literals.forEach { dslLiteral ->
            if (literalValue.equals(dslLiteral.literalValue, true)) {
                throw DuplicateLiteralException("Duplicate literal: '$literalValue'")
            }

            val duplicate = extraLiteralValues.find { it.equals(dslLiteral.literalValue, true) }

            if (duplicate != null) {
                throw DuplicateLiteralException("Duplicate literal: '$duplicate'")
            }
        }

        extraLiteralValues.forEach { extraLiteralValue ->
            if (extraLiteralValue.equals(literalValue, true)) {
                throw DuplicateLiteralException("Duplicate literal: '$extraLiteralValue'")
            }

            if (extraLiteralValues.count { extraLiteralValue.equals(it, true) } > 1) {
                throw DuplicateLiteralException("Duplicate literal: '$extraLiteralValue'")
            }
        }

        val literal = DSLCommandLiteral(this, literalValue)
        block.invoke(literal, DSLLiteralKey(literal, literalValue))
        literals.add(literal)


        for (extraLiteralValue in extraLiteralValues) {
            val extraLiteral = DSLCommandLiteral(this, extraLiteralValue)
            block.invoke(extraLiteral, DSLLiteralKey(extraLiteral, extraLiteralValue))
            literals.add(extraLiteral)
        }
    }

    inline fun <reified T: Any> argument(name: String, block: DSLCommandArgument<T>.(DSLArgumentKey<T>) -> Unit): DSLCommandArgument<T> {
        Check.all(name, "argument", Checks.notBlank, Checks.noWhitespace)

        val argumentType = T::class

        arguments.forEach {
            if (it.type == argumentType) {
                throw DuplicateArgumentTypeException("Duplicate argument type in same level: $argumentType")
            }
        }

        val argument = DSLCommandArgument(this, name, argumentType)
        block.invoke(argument, DSLArgumentKey(argument, name, argumentType))
        arguments.add(argument)
        return argument
    }

    inline fun <reified T> executes(noinline block: (CommandContext) -> T): DSLCommandExecutor<T> {
        if (executor != null) {
            throw DuplicateExecutorException("Executor already exists on level!")
        }
        val executor = DSLCommandExecutor(this, block)
        this.executor = executor
        return executor
    }
}