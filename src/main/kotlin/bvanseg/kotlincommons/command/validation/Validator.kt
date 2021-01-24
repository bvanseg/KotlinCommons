package bvanseg.kotlincommons.command.validation

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
interface Validator<T : Annotation, V : Any> {
    fun validate(annotation: T, value: V): Boolean

    fun createError(annotation: T, value: V): ValidationError =
        ValidationError("Validation failed for annotation $annotation with value $value!")
}
