package bvanseg.kotlincommons.lang.command.transformer

import bvanseg.kotlincommons.grouping.enum.enumValueOfOrNull
import bvanseg.kotlincommons.grouping.enum.getOrNull
import bvanseg.kotlincommons.lang.command.token.buffer.ArgumentTokenBuffer
import bvanseg.kotlincommons.lang.command.token.buffer.PeekingTokenBuffer
import kotlin.reflect.KClass

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
abstract class EnumTransformer<E : Enum<E>>(type: KClass<E>): Transformer<E>(type) {
    override fun matches(buffer: PeekingTokenBuffer): Boolean {
        val text = buffer.peek()?.value ?: return false
        val asInt = text.toIntOrNull()
        if (asInt != null && type.getOrNull(asInt) != null) {
            return true
        }
        return type.enumValueOfOrNull(buffer.peek()?.value ?: "", true) != null
    }

    override fun parse(buffer: ArgumentTokenBuffer): E {
        val nextTokenValue = buffer.next().value
        return type.getOrNull(nextTokenValue.toIntOrNull() ?: -1) ?: type.enumValueOfOrNull(nextTokenValue, true)!!
    }
}