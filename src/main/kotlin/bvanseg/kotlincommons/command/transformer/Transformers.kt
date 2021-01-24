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
package bvanseg.kotlincommons.command.transformer

import bvanseg.kotlincommons.command.validator.impl.ClampByte
import bvanseg.kotlincommons.command.validator.impl.ClampDouble
import bvanseg.kotlincommons.command.validator.impl.ClampFloat
import bvanseg.kotlincommons.command.validator.impl.ClampInt
import bvanseg.kotlincommons.command.validator.impl.ClampLong
import bvanseg.kotlincommons.command.validator.impl.ClampShort
import bvanseg.kotlincommons.command.context.Context
import bvanseg.kotlincommons.command.util.Argument
import bvanseg.kotlincommons.comparable.clampOrNull
import bvanseg.kotlincommons.string.remove
import bvanseg.kotlincommons.string.toURIOrNull
import bvanseg.kotlincommons.string.toURLOrNull
import java.math.BigDecimal
import java.math.BigInteger
import java.net.URI
import java.net.URL
import java.util.concurrent.TimeUnit
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation


private const val kotlinRangeDelimiter = ".."

/**
 * Built-in transformers provided for developers. Serve as both examples and utilities.
 *
 * @author bright_spark
 * @since 2.1.0
 */

object ByteTransformer : Transformer<Byte>(Byte::class) {
    override fun parse(input: String, ctx: Context?): Byte? = input.remove(",", "_").toByteOrNull()
}

object ShortTransformer : Transformer<Short>(Short::class) {
    override fun parse(input: String, ctx: Context?): Short? = input.remove(",", "_").toShortOrNull()
}

object IntTransformer : Transformer<Int>(Int::class) {
    override fun parse(input: String, ctx: Context?): Int? = input.remove(",", "_").toIntOrNull()
}

object LongTransformer : Transformer<Long>(Long::class) {
    override fun parse(input: String, ctx: Context?): Long? = input.remove(",", "_").toLongOrNull()
}

object FloatTransformer : Transformer<Float>(Float::class) {
    override fun parse(input: String, ctx: Context?): Float? = input.remove(",", "_").toFloatOrNull()
}

object DoubleTransformer : Transformer<Double>(Double::class) {
    override fun parse(input: String, ctx: Context?): Double? = input.remove(",", "_").toDoubleOrNull()
}

object CharTransformer : Transformer<Char>(Char::class) {
    override fun parse(input: String, ctx: Context?): Char? = input.singleOrNull()
}

object BooleanTransformer : Transformer<Boolean>(Boolean::class) {
    override fun parse(input: String, ctx: Context?): Boolean = input.toBoolean()
}

object StringTransformer : Transformer<String>(String::class) {
    override fun parse(input: String, ctx: Context?): String = input
}

object ArgumentTransformer : Transformer<Argument>(Argument::class) {
    override fun parse(input: String, ctx: Context?): Argument = Argument(input)
}

object BigIntegerTransformer : Transformer<BigInteger>(BigInteger::class) {
    override fun parse(input: String, ctx: Context?): BigInteger? = input.toBigIntegerOrNull()
}

object BigDecimalTransformer : Transformer<BigDecimal>(BigDecimal::class) {
    override fun parse(input: String, ctx: Context?): BigDecimal? = input.toBigDecimalOrNull()
}

object TimeUnitTransformer : EnumTransformer<TimeUnit>(TimeUnit::class)

object IntRangeTransformer : Transformer<IntRange>(IntRange::class) {
    override fun parse(input: String, ctx: Context?): IntRange? {
        val split = input.split(kotlinRangeDelimiter)
        val range = split[0].toIntOrNull() to split[1].toIntOrNull()

        if (range.first != null && range.second != null) {
            return IntRange(range.first!!, range.second!!)
        }

        return null
    }
}

@ExperimentalUnsignedTypes
object UIntRangeTransformer : Transformer<UIntRange>(UIntRange::class) {
    override fun parse(input: String, ctx: Context?): UIntRange? {
        val split = input.split(kotlinRangeDelimiter)
        val range = split[0].toUIntOrNull() to split[1].toUIntOrNull()

        if (range.first != null && range.second != null) {
            return UIntRange(range.first!!, range.second!!)
        }

        return null
    }
}

object LongRangeTransformer : Transformer<LongRange>(LongRange::class) {
    override fun parse(input: String, ctx: Context?): LongRange? {
        val split = input.split(kotlinRangeDelimiter)
        val range = split[0].toLongOrNull() to split[1].toLongOrNull()

        if (range.first != null && range.second != null) {
            return LongRange(range.first!!, range.second!!)
        }

        return null
    }
}

@ExperimentalUnsignedTypes
object ULongRangeTransformer : Transformer<ULongRange>(ULongRange::class) {
    override fun parse(input: String, ctx: Context?): ULongRange? {
        val split = input.split(kotlinRangeDelimiter)
        val range = split[0].toULongOrNull() to split[1].toULongOrNull()

        if (range.first != null && range.second != null) {
            return ULongRange(range.first!!, range.second!!)
        }

        return null
    }
}

object URITransformer : Transformer<URI>(URI::class) {
    override fun parse(input: String, ctx: Context?): URI? = input.toURIOrNull()
}

object URLTransformer : Transformer<URL>(URL::class) {
    override fun parse(input: String, ctx: Context?): URL? = input.toURLOrNull()
}
