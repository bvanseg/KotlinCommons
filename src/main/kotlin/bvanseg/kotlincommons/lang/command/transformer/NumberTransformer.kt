package bvanseg.kotlincommons.lang.command.transformer

import bvanseg.kotlincommons.lang.command.context.CommandContext
import bvanseg.kotlincommons.lang.command.exception.TransformerException
import bvanseg.kotlincommons.lang.command.token.buffer.ArgumentTokenBuffer
import bvanseg.kotlincommons.lang.command.token.buffer.PeekingTokenBuffer
import bvanseg.kotlincommons.math.format
import java.math.BigInteger
import kotlin.reflect.KClass

/**
 * @author Mark Passey
 * @since 2.10.0
 */
abstract class NumberTransformer<T : Any> : Transformer<T> {
	companion object {
		val REGEX = Regex("^[+-]?\\d+\$")
	}

	private val range: ClosedRange<BigInteger>
	private val stringToNum: (String) -> T

	constructor(type: KClass<T>, min: Number, max: Number, stringToNum: (String) -> T) :
			this(type, min.toLong().toBigInteger(), max.toLong().toBigInteger(), stringToNum)

	constructor(type: KClass<T>, min: BigInteger, max: BigInteger, stringToNum: (String) -> T) : super(type) {
		range = min..max
		this.stringToNum = stringToNum
	}

	override fun matches(buffer: PeekingTokenBuffer, context: CommandContext): Boolean =
		buffer.peek()?.value?.matches(REGEX) ?: false

	override fun parse(buffer: ArgumentTokenBuffer, context: CommandContext): T {
		val text = buffer.next().value
		val bigInt = text.toBigInteger()
		if (bigInt !in range) {
			throw TransformerException("Value '$bigInt' does not fall within the range of (${range.start.format()} to ${range.endInclusive.format()})")
		}
		return stringToNum(text)
	}
}
