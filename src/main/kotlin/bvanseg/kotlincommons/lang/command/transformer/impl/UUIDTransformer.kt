package bvanseg.kotlincommons.lang.command.transformer.impl

import bvanseg.kotlincommons.lang.command.transformer.Transformer
import java.util.UUID

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
object UUIDTransformer: Transformer<UUID>(UUID::class) {
    private val REGEX = Regex("^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$")
    override fun matches(input: String): Boolean = input.matches(REGEX)
    override fun parse(input: String): UUID = UUID.fromString(input)
}