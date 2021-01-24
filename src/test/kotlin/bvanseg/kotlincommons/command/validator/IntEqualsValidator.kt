package bvanseg.kotlincommons.command.validator

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
class IntEqualsValidator : Validator<IntEquals, Int> {
    // Transform whatever number is passed in to the annotation's number.
    override fun mutate(annotation: IntEquals, value: Int): Int = value * 2

    override fun validate(annotation: IntEquals, value: Int): Boolean = value == annotation.num

    override fun createError(annotation: IntEquals, value: Int): ValidationError =
        ValidationError("Expected value of ${annotation.num} but got $value instead.")
}