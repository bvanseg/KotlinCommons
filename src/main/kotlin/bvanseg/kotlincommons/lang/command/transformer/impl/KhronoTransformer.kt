package bvanseg.kotlincommons.lang.command.transformer.impl

import bvanseg.kotlincommons.lang.command.token.buffer.ArgumentTokenBuffer
import bvanseg.kotlincommons.lang.command.token.buffer.PeekingTokenBuffer
import bvanseg.kotlincommons.lang.command.transformer.Transformer
import bvanseg.kotlincommons.time.api.Khrono

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
object KhronoTransformer: Transformer<Khrono>(Khrono::class) {
    override fun matches(buffer: PeekingTokenBuffer): Boolean = try {
        val text = buffer.peek()?.value
        if (text.equals("now", true)) {
            true
        } else {
            Khrono.parse(text ?: "")
            true
        }
    } catch(e: Exception) { false }
    override fun parse(buffer: ArgumentTokenBuffer): Khrono {
        val text = buffer.next().value

        return if (text.equals("now", true)) {
            Khrono.now()
        } else {
            Khrono.parse(text)
        }
    }
}