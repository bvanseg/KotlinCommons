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

import bvanseg.kotlincommons.command.annotation.ByteRange
import bvanseg.kotlincommons.command.annotation.DoubleRange
import bvanseg.kotlincommons.command.annotation.FloatRange
import bvanseg.kotlincommons.command.annotation.IntRange
import bvanseg.kotlincommons.command.annotation.LongRange
import bvanseg.kotlincommons.command.annotation.ShortRange
import bvanseg.kotlincommons.command.context.Context
import bvanseg.kotlincommons.command.util.Argument
import bvanseg.kotlincommons.comparable.clampOrNull
import bvanseg.kotlincommons.string.remove
import bvanseg.kotlincommons.string.toURI
import bvanseg.kotlincommons.string.toURIOrNull
import bvanseg.kotlincommons.string.toURL
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
object IntTransformer : Transformer<Int>(Int::class) {
    override fun parse(parameter: KParameter, input: String, ctx: Context?): Int? {
        val value = input.remove(",", "_").toIntOrNull()
        return parameter.findAnnotation<IntRange>()?.let { clampOrNull(value, it.min, it.max) } ?: value
    }

    override fun parse(input: String, ctx: Context?): Int? = input.remove(",", "_").toIntOrNull()
}

object DoubleTransformer : Transformer<Double>(Double::class) {
    override fun parse(parameter: KParameter, input: String, ctx: Context?): Double? {
        val value = input.remove(",", "_").toDoubleOrNull()
        return parameter.findAnnotation<DoubleRange>()?.let { clampOrNull(value, it.min, it.max) } ?: value
    }

    override fun parse(input: String, ctx: Context?): Double? = input.remove(",", "_").toDoubleOrNull()
}

object FloatTransformer : Transformer<Float>(Float::class) {
    override fun parse(parameter: KParameter, input: String, ctx: Context?): Float? {
        val value = input.remove(",", "_").toFloatOrNull()
        return parameter.findAnnotation<FloatRange>()?.let { clampOrNull(value, it.min, it.max) } ?: value
    }

    override fun parse(input: String, ctx: Context?): Float? = input.remove(",", "_").toFloatOrNull()
}

object LongTransformer : Transformer<Long>(Long::class) {
    override fun parse(parameter: KParameter, input: String, ctx: Context?): Long? {
        val value = input.remove(",", "_").toLongOrNull()
        return parameter.findAnnotation<LongRange>()?.let { clampOrNull(value, it.min, it.max) } ?: value
    }

    override fun parse(input: String, ctx: Context?): Long? = input.remove(",", "_").toLongOrNull()
}

object ShortTransformer : Transformer<Short>(Short::class) {
    override fun parse(parameter: KParameter, input: String, ctx: Context?): Short? {
        val value = input.remove(",", "_").toShortOrNull()
        return parameter.findAnnotation<ShortRange>()?.let { clampOrNull(value, it.min, it.max) } ?: value
    }

    override fun parse(input: String, ctx: Context?): Short? = input.remove(",", "_").toShortOrNull()
}

object ByteTransformer : Transformer<Byte>(Byte::class) {
    override fun parse(parameter: KParameter, input: String, ctx: Context?): Byte? {
        val value = input.remove(",", "_").toByteOrNull()
        return parameter.findAnnotation<ByteRange>()?.let { clampOrNull(value, it.min, it.max) } ?: value
    }

    override fun parse(input: String, ctx: Context?): Byte? = input.remove(",", "_").toByteOrNull()
}

object CharTransformer : Transformer<Char>(Char::class) {
    override fun parse(input: String, ctx: Context?): Char? = input.singleOrNull()
}

object BooleanTransformer : Transformer<Boolean>(Boolean::class) {
    override fun parse(input: String, ctx: Context?): Boolean? = input.toBoolean()
}

object StringTransformer : Transformer<String>(String::class) {
    override fun parse(input: String, ctx: Context?): String? = input
}

object ArgumentTransformer : Transformer<Argument>(Argument::class) {
    override fun parse(input: String, ctx: Context?): Argument? = Argument(input)
}

object BigIntegerTransformer : Transformer<BigInteger>(BigInteger::class) {
    override fun parse(input: String, ctx: Context?): BigInteger? = input.toBigIntegerOrNull()
}

object BigDecimalTransformer : Transformer<BigDecimal>(BigDecimal::class) {
    override fun parse(input: String, ctx: Context?): BigDecimal? = input.toBigDecimalOrNull()
}

object TimeUnitTransformer : EnumTransformer<TimeUnit>(TimeUnit::class)

object IntRangeTransformer : Transformer<kotlin.ranges.IntRange>(kotlin.ranges.IntRange::class) {
    override fun parse(input: String, ctx: Context?): kotlin.ranges.IntRange? {
        val split = input.split(kotlinRangeDelimiter)
        val range = split[0].toIntOrNull() to split[1].toIntOrNull()

        if (range.first != null && range.second != null) {
            return kotlin.ranges.IntRange(range.first!!, range.second!!)
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

object LongRangeTransformer : Transformer<kotlin.ranges.LongRange>(kotlin.ranges.LongRange::class) {
    override fun parse(input: String, ctx: Context?): kotlin.ranges.LongRange? {
        val split = input.split(kotlinRangeDelimiter)
        val range = split[0].toLongOrNull() to split[1].toLongOrNull()

        if (range.first != null && range.second != null) {
            return kotlin.ranges.LongRange(range.first!!, range.second!!)
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
