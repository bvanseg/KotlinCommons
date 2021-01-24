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
package bvanseg.kotlincommons.command.validator.impl

import bvanseg.kotlincommons.command.validator.ValidationError
import bvanseg.kotlincommons.command.validator.Validator
import bvanseg.kotlincommons.comparable.clamp

// Built-in annotations for usage on numeric parameters. Bound the number between a min and max prior to function invocation.

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class ClampByte(val min: Byte, val max: Byte)

object ClampByteValidator: Validator<ClampByte, Byte> {
    override fun mutate(annotation: ClampByte, value: Byte): Byte = clamp(value, annotation.min, annotation.max)
    override fun validate(annotation: ClampByte, value: Byte): Boolean = value >= annotation.min && value <= annotation.max
    override fun createError(annotation: ClampByte, value: Byte): ValidationError = ValidationError("Value $value does not fall within the range of ${annotation.min} to ${annotation.max}.")
}

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class ClampShort(val min: Short, val max: Short)

object ClampShortValidator: Validator<ClampShort, Short> {
    override fun mutate(annotation: ClampShort, value: Short): Short = clamp(value, annotation.min, annotation.max)
    override fun validate(annotation: ClampShort, value: Short): Boolean = value >= annotation.min && value <= annotation.max
    override fun createError(annotation: ClampShort, value: Short): ValidationError = ValidationError("Value $value does not fall within the range of ${annotation.min} to ${annotation.max}.")
}

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class ClampInt(val min: Int, val max: Int)

object ClampIntValidator: Validator<ClampInt, Int> {
    override fun mutate(annotation: ClampInt, value: Int): Int = clamp(value, annotation.min, annotation.max)
    override fun validate(annotation: ClampInt, value: Int): Boolean = value >= annotation.min && value <= annotation.max
    override fun createError(annotation: ClampInt, value: Int): ValidationError = ValidationError("Value $value does not fall within the range of ${annotation.min} to ${annotation.max}.")
}

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class ClampLong(val min: Long, val max: Long)

object ClampLongValidator: Validator<ClampLong, Long> {
    override fun mutate(annotation: ClampLong, value: Long): Long = clamp(value, annotation.min, annotation.max)
    override fun validate(annotation: ClampLong, value: Long): Boolean = value >= annotation.min && value <= annotation.max
    override fun createError(annotation: ClampLong, value: Long): ValidationError = ValidationError("Value $value does not fall within the range of ${annotation.min} to ${annotation.max}.")
}

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class ClampFloat(val min: Float, val max: Float)

object ClampFloatValidator: Validator<ClampFloat, Float> {
    override fun mutate(annotation: ClampFloat, value: Float): Float = clamp(value, annotation.min, annotation.max)
    override fun validate(annotation: ClampFloat, value: Float): Boolean = value >= annotation.min && value <= annotation.max
    override fun createError(annotation: ClampFloat, value: Float): ValidationError = ValidationError("Value $value does not fall within the range of ${annotation.min} to ${annotation.max}.")
}

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class ClampDouble(val min: Double, val max: Double)

object ClampDoubleValidator: Validator<ClampDouble, Double> {
    override fun mutate(annotation: ClampDouble, value: Double): Double = clamp(value, annotation.min, annotation.max)
    override fun validate(annotation: ClampDouble, value: Double): Boolean = value >= annotation.min && value <= annotation.max
    override fun createError(annotation: ClampDouble, value: Double): ValidationError = ValidationError("Value $value does not fall within the range of ${annotation.min} to ${annotation.max}.")
}