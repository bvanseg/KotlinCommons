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

    inline fun <reified T: Any, reified U: Any> arguments(
        arg1Name: String,
        arg2Name: String,
        block: DSLCommandArgument<U>.(DSLArgumentKey<T>, DSLArgumentKey<U>) -> Unit
    ): DSLCommandArgument<U> {
        Check.all(arg1Name, "argument1", Checks.notBlank, Checks.noWhitespace)
        Check.all(arg2Name, "argument2", Checks.notBlank, Checks.noWhitespace)

        val argument1Type = T::class
        val argument2Type = U::class

        arguments.forEach {
            if (it.type == argument1Type) {
                throw DuplicateArgumentTypeException("Duplicate argument type in same level: $argument1Type")
            }
            if (it.type == argument2Type) {
                throw DuplicateArgumentTypeException("Duplicate argument type in same level: $argument2Type")
            }
        }

        val argument1 = DSLCommandArgument(this, arg1Name, argument1Type)
        val argument2 = DSLCommandArgument(this, arg2Name, argument2Type)

        block.invoke(argument2, DSLArgumentKey(argument1, arg1Name, argument1Type), DSLArgumentKey(argument2, arg2Name, argument2Type))
        arguments.add(argument1)
        argument1.arguments.add(argument2)

        return argument2
    }

    inline fun <reified T: Any, reified U: Any, reified V: Any> arguments(
        arg1Name: String,
        arg2Name: String,
        arg3Name: String,
        block: DSLCommandArgument<V>.(DSLArgumentKey<T>, DSLArgumentKey<U>, DSLArgumentKey<V>) -> Unit
    ): DSLCommandArgument<V> {
        Check.all(arg1Name, "argument1", Checks.notBlank, Checks.noWhitespace)
        Check.all(arg2Name, "argument2", Checks.notBlank, Checks.noWhitespace)
        Check.all(arg3Name, "argument3", Checks.notBlank, Checks.noWhitespace)

        val argument1Type = T::class
        val argument2Type = U::class
        val argument3Type = V::class

        arguments.forEach {
            if (it.type == argument1Type) {
                throw DuplicateArgumentTypeException("Duplicate argument type in same level: $argument1Type")
            }
            if (it.type == argument2Type) {
                throw DuplicateArgumentTypeException("Duplicate argument type in same level: $argument2Type")
            }
            if (it.type == argument3Type) {
                throw DuplicateArgumentTypeException("Duplicate argument type in same level: $argument3Type")
            }
        }

        val argument1 = DSLCommandArgument(this, arg1Name, argument1Type)
        val argument2 = DSLCommandArgument(this, arg2Name, argument2Type)
        val argument3 = DSLCommandArgument(this, arg3Name, argument3Type)

        block.invoke(argument3,
            DSLArgumentKey(argument1, arg1Name, argument1Type),
            DSLArgumentKey(argument2, arg2Name, argument2Type),
            DSLArgumentKey(argument3, arg3Name, argument3Type))
        arguments.add(argument1)
        argument1.arguments.add(argument2)
        argument2.arguments.add(argument3)

        return argument3
    }

    inline fun <reified T: Any, reified U: Any, reified V: Any, reified W: Any> arguments(
        arg1Name: String,
        arg2Name: String,
        arg3Name: String,
        arg4Name: String,
        block: DSLCommandArgument<W>.(DSLArgumentKey<T>, DSLArgumentKey<U>, DSLArgumentKey<V>, DSLArgumentKey<W>) -> Unit
    ): DSLCommandArgument<W> {
        Check.all(arg1Name, "argument1", Checks.notBlank, Checks.noWhitespace)
        Check.all(arg2Name, "argument2", Checks.notBlank, Checks.noWhitespace)
        Check.all(arg3Name, "argument3", Checks.notBlank, Checks.noWhitespace)
        Check.all(arg4Name, "argument4", Checks.notBlank, Checks.noWhitespace)

        val argument1Type = T::class
        val argument2Type = U::class
        val argument3Type = V::class
        val argument4Type = W::class

        arguments.forEach {
            if (it.type == argument1Type) {
                throw DuplicateArgumentTypeException("Duplicate argument type in same level: $argument1Type")
            }
            if (it.type == argument2Type) {
                throw DuplicateArgumentTypeException("Duplicate argument type in same level: $argument2Type")
            }
            if (it.type == argument3Type) {
                throw DuplicateArgumentTypeException("Duplicate argument type in same level: $argument3Type")
            }
            if (it.type == argument4Type) {
                throw DuplicateArgumentTypeException("Duplicate argument type in same level: $argument4Type")
            }
        }

        val argument1 = DSLCommandArgument(this, arg1Name, argument1Type)
        val argument2 = DSLCommandArgument(this, arg2Name, argument2Type)
        val argument3 = DSLCommandArgument(this, arg3Name, argument3Type)
        val argument4 = DSLCommandArgument(this, arg4Name, argument4Type)

        block.invoke(argument4,
            DSLArgumentKey(argument1, arg1Name, argument1Type),
            DSLArgumentKey(argument2, arg2Name, argument2Type),
            DSLArgumentKey(argument3, arg3Name, argument3Type),
            DSLArgumentKey(argument4, arg4Name, argument4Type))
        arguments.add(argument1)
        argument1.arguments.add(argument2)
        argument2.arguments.add(argument3)
        argument3.arguments.add(argument4)

        return argument4
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