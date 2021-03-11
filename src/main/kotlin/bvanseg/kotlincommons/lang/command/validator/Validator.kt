package bvanseg.kotlincommons.lang.command.validator

import kotlin.reflect.KClass

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
abstract class Validator<T: Any>(val type: KClass<T>) {
    abstract fun validate(value: T): ValidationResult
}