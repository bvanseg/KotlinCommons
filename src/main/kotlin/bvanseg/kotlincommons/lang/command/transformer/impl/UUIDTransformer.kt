package bvanseg.kotlincommons.lang.command.transformer.impl

import bvanseg.kotlincommons.lang.command.token.buffer.ArgumentTokenBuffer
import bvanseg.kotlincommons.lang.command.token.buffer.PeekingTokenBuffer
import bvanseg.kotlincommons.lang.command.transformer.Transformer
import java.util.UUID

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
object UUIDTransformer: Transformer<UUID>(UUID::class) {
    private val REGEX = Regex("^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$")
    override fun matches(buffer: PeekingTokenBuffer): Boolean = buffer.peek()?.value?.matches(REGEX) ?: false
    override fun parse(buffer: ArgumentTokenBuffer): UUID = UUID.fromString(buffer.next().value)
}