package bvanseg.kotlincommons.lang.command.dsl

import bvanseg.kotlincommons.lang.command.validator.Validator
import kotlin.reflect.KClass

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
class DSLCommandArgument<T: Any>(val parent: DSLCommandNode, val name: String, val type: KClass<T>): DSLCommandNode() {
    val validators: HashSet<Validator<T>> = hashSetOf()

    fun validate(validator: Validator<T>, vararg additionalValidators: Validator<T>) {
        validators.add(validator)
        validators.addAll(additionalValidators)
    }
}