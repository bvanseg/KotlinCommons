package bvanseg.kotlincommons.lang.command.validator

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
interface Validator<T: Any> {
    fun validate(value: T): Boolean
}