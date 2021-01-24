package bvanseg.kotlincommons.command.validator

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
interface Validator<T : Annotation, V : Any> {

    /**
     * Takes the given value and changes or discards it. This fires before [validate].
     * Default behavior is to return the original value.
     *
     * @param annotation The annotation triggering the validator.
     * @param value The value to change or replace.
     *
     * @return A changed or new [value].
     */
    fun mutate(annotation: T, value: V): V = value

    /**
     * Takes a given value and performs checks on it to make sure it is valid.
     *
     * @param annotation The annotation triggering the validator.
     * @param value The value to validate.
     *
     * @return True or false if the given [value] is valid.
     */
    fun validate(annotation: T, value: V): Boolean

    /**
     * Creates an error message caused by the validation step failing. This function will
     * only be called if [validate] returns false.
     *
     * @param annotation The annotation triggering the validator.
     * @param value The value that is invalid.
     *
     * @return A [ValidationError].
     */
    fun createError(annotation: T, value: V): ValidationError =
        ValidationError("Validation failed for annotation $annotation with value $value!")
}
