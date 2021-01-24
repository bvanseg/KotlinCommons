package bvanseg.kotlincommons.command.validation

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
class IntEqualsValidator : Validator<IntEquals, Int> {
    override fun validate(annotation: IntEquals, value: Int): Boolean = value == annotation.num

    override fun createError(annotation: IntEquals, value: Int): ValidationError =
        ValidationError("Expected value of ${annotation.num} but got $value instead.")
}