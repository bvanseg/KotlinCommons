package bvanseg.kotlincommons.util.command.validator

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
abstract class Validator<T : Annotation, V : Any>(val type: Class<T>) {

    /**
     * Takes the given value and changes or discards it. This fires before [validate].
     * Default behavior is to return the original value.
     *
     * @param annotation The annotation triggering the validator.
     * @param value The value to change or replace.
     *
     * @return A changed or new [value].
     */
    open fun mutate(annotation: T, value: V): V = value

    /**
     * Takes a given value and performs checks on it to make sure it is valid.
     *
     * @param annotation The annotation triggering the validator.
     * @param value The value to validate.
     *
     * @return True or false if the given [value] is valid.
     */
    abstract fun validate(annotation: T, value: V): Boolean

    /**
     * Creates an error message caused by the validation step failing. This function will
     * only be called if [validate] returns false.
     *
     * @param annotation The annotation triggering the validator.
     * @param value The value that is invalid.
     *
     * @return A [ValidationError].
     */
    open fun createError(annotation: T, value: V): ValidationError =
        ValidationError("Validation failed for annotation $annotation with value $value!")
}
