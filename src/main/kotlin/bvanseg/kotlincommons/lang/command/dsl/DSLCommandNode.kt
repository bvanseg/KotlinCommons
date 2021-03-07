package bvanseg.kotlincommons.lang.command.dsl

import bvanseg.kotlincommons.lang.check.Check
import bvanseg.kotlincommons.lang.check.Checks
import bvanseg.kotlincommons.lang.command.DuplicateArgumentTypeException
import bvanseg.kotlincommons.lang.command.DuplicateExecutorException
import bvanseg.kotlincommons.lang.command.DuplicateLiteralException
import bvanseg.kotlincommons.lang.command.context.CommandContext
import java.lang.RuntimeException

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
abstract class DSLCommandNode {
    val literals: MutableList<DSLCommandLiteral> = mutableListOf()
    val arguments: MutableList<DSLCommandArgument<*>> = mutableListOf()
    var executor: DSLCommandExecutor<*>? = null

    fun literal(literalValue: String, vararg extraLiteralValues: String, block: DSLCommandLiteral.(DSLKey<String>) -> Unit) {
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
        block.invoke(literal, DSLKey(literalValue, String::class))
        literals.add(literal)


        for (extraLiteralValue in extraLiteralValues) {
            val extraLiteral = DSLCommandLiteral(this, extraLiteralValue)
            block.invoke(extraLiteral, DSLKey(extraLiteralValue, String::class))
            literals.add(extraLiteral)
        }
    }

    inline fun <reified T: Any> argument(name: String, block: DSLCommandArgument<T>.(DSLKey<T>) -> Unit): DSLCommandArgument<T> {
        Check.all(name, "argument", Checks.notBlank, Checks.noWhitespace)

        val argumentType = T::class

        arguments.forEach {
            if (it.type == argumentType) {
                throw DuplicateArgumentTypeException("Duplicate argument type in same level: $argumentType")
            }
        }

        val argument = DSLCommandArgument(this, name, argumentType)
        block.invoke(argument, DSLKey(name, argumentType))
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