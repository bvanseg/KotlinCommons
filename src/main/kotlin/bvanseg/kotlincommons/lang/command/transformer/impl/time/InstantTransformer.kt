package bvanseg.kotlincommons.lang.command.transformer.impl.time

import bvanseg.kotlincommons.lang.command.token.buffer.ArgumentTokenBuffer
import bvanseg.kotlincommons.lang.command.token.buffer.PeekingTokenBuffer
import bvanseg.kotlincommons.lang.command.transformer.Transformer
import java.time.Instant

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
object InstantTransformer: Transformer<Instant>(Instant::class) {
    override fun matches(buffer: PeekingTokenBuffer): Boolean = try {
        val text = buffer.peek()?.value
        if (text.equals("now", true)) {
            true
        } else {
            Instant.parse(text ?: "")
            true
        }
    } catch(e: Exception) { false }
    override fun parse(buffer: ArgumentTokenBuffer): Instant {
        val text = buffer.next().value

        return if (text.equals("now", true)) {
            Instant.now()
        } else {
            Instant.parse(text)
        }
    }
}