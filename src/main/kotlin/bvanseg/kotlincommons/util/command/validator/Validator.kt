/*
 * MIT License
 *
 * Copyright (c) 2021 Boston Vanseghi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
