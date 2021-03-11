package bvanseg.kotlincommons.lang.command.transformer.impl

import bvanseg.kotlincommons.lang.command.token.buffer.ArgumentTokenBuffer
import bvanseg.kotlincommons.lang.command.token.buffer.PeekingTokenBuffer
import bvanseg.kotlincommons.lang.command.transformer.Transformer
import java.time.LocalDate

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
object LocalDateTransformer: Transformer<LocalDate>(LocalDate::class) {
    override fun matches(buffer: PeekingTokenBuffer): Boolean = try {
        val text = buffer.peek()?.value
        if (text.equals("now", true)) {
            true
        } else {
            LocalDate.parse(text ?: "")
            true
        }
    } catch(e: Exception) { false }
    override fun parse(buffer: ArgumentTokenBuffer): LocalDate {
        val text = buffer.next().value

        return if (text.equals("now", true)) {
            LocalDate.now()
        } else {
            LocalDate.parse(text)
        }
    }
}