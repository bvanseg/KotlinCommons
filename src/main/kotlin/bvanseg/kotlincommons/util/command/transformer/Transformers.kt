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
package bvanseg.kotlincommons.util.command.transformer

import bvanseg.kotlincommons.lang.string.remove
import bvanseg.kotlincommons.lang.string.toURIOrNull
import bvanseg.kotlincommons.lang.string.toURLOrNull
import bvanseg.kotlincommons.util.command.context.Context
import bvanseg.kotlincommons.util.command.util.Argument
import java.math.BigDecimal
import java.math.BigInteger
import java.net.URI
import java.net.URL
import java.util.concurrent.TimeUnit


@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
private const val kotlinRangeDelimiter = ".."

/**
 * Built-in transformers provided for developers. Serve as both examples and utilities.
 *
 * @author bright_spark
 * @since 2.1.0
 */

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
object ByteTransformer : Transformer<Byte>(Byte::class) {
    override fun parse(input: String, ctx: Context?): Byte? = input.remove(",", "_").toByteOrNull()
}

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
object ShortTransformer : Transformer<Short>(Short::class) {
    override fun parse(input: String, ctx: Context?): Short? = input.remove(",", "_").toShortOrNull()
}

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
object IntTransformer : Transformer<Int>(Int::class) {
    override fun parse(input: String, ctx: Context?): Int? = input.remove(",", "_").toIntOrNull()
}

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
object LongTransformer : Transformer<Long>(Long::class) {
    override fun parse(input: String, ctx: Context?): Long? = input.remove(",", "_").toLongOrNull()
}

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
object FloatTransformer : Transformer<Float>(Float::class) {
    override fun parse(input: String, ctx: Context?): Float? = input.remove(",", "_").toFloatOrNull()
}

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
object DoubleTransformer : Transformer<Double>(Double::class) {
    override fun parse(input: String, ctx: Context?): Double? = input.remove(",", "_").toDoubleOrNull()
}

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
object CharTransformer : Transformer<Char>(Char::class) {
    override fun parse(input: String, ctx: Context?): Char? = input.singleOrNull()
}

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
object BooleanTransformer : Transformer<Boolean>(Boolean::class) {
    override fun parse(input: String, ctx: Context?): Boolean = input.toBoolean()
}

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
object StringTransformer : Transformer<String>(String::class) {
    override fun parse(input: String, ctx: Context?): String = input
}

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
object ArgumentTransformer : Transformer<Argument>(Argument::class) {
    override fun parse(input: String, ctx: Context?): Argument = Argument(input)
}

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
object BigIntegerTransformer : Transformer<BigInteger>(BigInteger::class) {
    override fun parse(input: String, ctx: Context?): BigInteger? = input.toBigIntegerOrNull()
}

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
object BigDecimalTransformer : Transformer<BigDecimal>(BigDecimal::class) {
    override fun parse(input: String, ctx: Context?): BigDecimal? = input.toBigDecimalOrNull()
}

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
object TimeUnitTransformer : EnumTransformer<TimeUnit>(TimeUnit::class)

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
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

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
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

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
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

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
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

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
object URITransformer : Transformer<URI>(URI::class) {
    override fun parse(input: String, ctx: Context?): URI? = input.toURIOrNull()
}

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
object URLTransformer : Transformer<URL>(URL::class) {
    override fun parse(input: String, ctx: Context?): URL? = input.toURLOrNull()
}
