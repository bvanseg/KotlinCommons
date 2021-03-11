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
package bvanseg.kotlincommons.util.command.validator.impl

import bvanseg.kotlincommons.util.command.validator.ValidationError
import bvanseg.kotlincommons.util.command.validator.Validator
import bvanseg.kotlincommons.util.comparable.clamp

// Built-in annotations for usage on numeric parameters. Bound the number between a min and max prior to function invocation.

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class ClampByte(val min: Byte, val max: Byte)

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
object ClampByteValidator : Validator<ClampByte, Byte>(ClampByte::class.java) {
    override fun mutate(annotation: ClampByte, value: Byte): Byte = clamp(value, annotation.min, annotation.max)
    override fun validate(annotation: ClampByte, value: Byte): Boolean =
        value >= annotation.min && value <= annotation.max

    override fun createError(annotation: ClampByte, value: Byte): ValidationError =
        ValidationError("Value $value does not fall within the range of ${annotation.min} to ${annotation.max}.")
}

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class ClampShort(val min: Short, val max: Short)

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
object ClampShortValidator : Validator<ClampShort, Short>(ClampShort::class.java) {
    override fun mutate(annotation: ClampShort, value: Short): Short = clamp(value, annotation.min, annotation.max)
    override fun validate(annotation: ClampShort, value: Short): Boolean =
        value >= annotation.min && value <= annotation.max

    override fun createError(annotation: ClampShort, value: Short): ValidationError =
        ValidationError("Value $value does not fall within the range of ${annotation.min} to ${annotation.max}.")
}

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class ClampInt(val min: Int, val max: Int)

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
object ClampIntValidator : Validator<ClampInt, Int>(ClampInt::class.java) {
    override fun mutate(annotation: ClampInt, value: Int): Int = clamp(value, annotation.min, annotation.max)
    override fun validate(annotation: ClampInt, value: Int): Boolean =
        value >= annotation.min && value <= annotation.max

    override fun createError(annotation: ClampInt, value: Int): ValidationError =
        ValidationError("Value $value does not fall within the range of ${annotation.min} to ${annotation.max}.")
}

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class ClampLong(val min: Long, val max: Long)

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
object ClampLongValidator : Validator<ClampLong, Long>(ClampLong::class.java) {
    override fun mutate(annotation: ClampLong, value: Long): Long = clamp(value, annotation.min, annotation.max)
    override fun validate(annotation: ClampLong, value: Long): Boolean =
        value >= annotation.min && value <= annotation.max

    override fun createError(annotation: ClampLong, value: Long): ValidationError =
        ValidationError("Value $value does not fall within the range of ${annotation.min} to ${annotation.max}.")
}

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class ClampFloat(val min: Float, val max: Float)

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
object ClampFloatValidator : Validator<ClampFloat, Float>(ClampFloat::class.java) {
    override fun mutate(annotation: ClampFloat, value: Float): Float = clamp(value, annotation.min, annotation.max)
    override fun validate(annotation: ClampFloat, value: Float): Boolean =
        value >= annotation.min && value <= annotation.max

    override fun createError(annotation: ClampFloat, value: Float): ValidationError =
        ValidationError("Value $value does not fall within the range of ${annotation.min} to ${annotation.max}.")
}

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class ClampDouble(val min: Double, val max: Double)

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
object ClampDoubleValidator : Validator<ClampDouble, Double>(ClampDouble::class.java) {
    override fun mutate(annotation: ClampDouble, value: Double): Double = clamp(value, annotation.min, annotation.max)
    override fun validate(annotation: ClampDouble, value: Double): Boolean =
        value >= annotation.min && value <= annotation.max

    override fun createError(annotation: ClampDouble, value: Double): ValidationError =
        ValidationError("Value $value does not fall within the range of ${annotation.min} to ${annotation.max}.")
}