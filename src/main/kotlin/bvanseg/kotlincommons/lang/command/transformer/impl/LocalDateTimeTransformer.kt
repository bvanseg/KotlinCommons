package bvanseg.kotlincommons.lang.command.transformer.impl

import bvanseg.kotlincommons.lang.command.token.buffer.ArgumentTokenBuffer
import bvanseg.kotlincommons.lang.command.token.buffer.PeekingTokenBuffer
import bvanseg.kotlincommons.lang.command.transformer.Transformer
import java.time.LocalDateTime

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
object LocalDateTimeTransformer: Transformer<LocalDateTime>(LocalDateTime::class) {
    override fun matches(buffer: PeekingTokenBuffer): Boolean = try {
        val text = buffer.peek()?.value
        if (text.equals("now", true)) {
            true
        } else {
            LocalDateTime.parse(text ?: "")
            true
        }
    } catch(e: Exception) { false }
    override fun parse(buffer: ArgumentTokenBuffer): LocalDateTime {
        val text = buffer.next().value

        return if (text.equals("now", true)) {
            LocalDateTime.now()
        } else {
            LocalDateTime.parse(text)
        }
    }
}