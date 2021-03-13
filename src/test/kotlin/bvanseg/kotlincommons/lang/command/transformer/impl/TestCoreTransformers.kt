package bvanseg.kotlincommons.lang.command.transformer.impl

import bvanseg.kotlincommons.graphic.Color
import bvanseg.kotlincommons.lang.command.token.Token
import bvanseg.kotlincommons.lang.command.token.TokenType
import bvanseg.kotlincommons.lang.command.token.buffer.ArgumentTokenBuffer
import bvanseg.kotlincommons.lang.string.toURL
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.BigInteger
import java.util.UUID

/**
 * @author Boston Vanseghi
 * @since 2.10.0
 */
class TestCoreTransformers {

    @Test
    fun testArgumentTokenBufferTransformer() {
        // GIVEN
        val transformer = ArgumentTokenBufferTransformer

        // WHEN
        val input = "foo"
        val buffer = ArgumentTokenBuffer(listOf(Token(input, TokenType.SINGLE_STRING)))

        // THEN
        Assertions.assertTrue(transformer.matches(buffer))
        Assertions.assertEquals(buffer, transformer.parse(buffer))
    }

    @Test
    fun testBigIntegerTransformer() {
        // GIVEN
        val transformer = BigIntegerTransformer

        // WHEN
        val input = BigInteger("999999999999999999999999999999999999")
        val buffer = ArgumentTokenBuffer(listOf(Token(input.toString(), TokenType.SINGLE_STRING)))

        // THEN
        Assertions.assertTrue(transformer.matches(buffer))
        Assertions.assertEquals(input, transformer.parse(buffer))
    }

    @Test
    fun testBigDecimalTransformer() {
        // GIVEN
        val transformer = BigDecimalTransformer

        // WHEN
        val input = BigDecimal("9.000000000099999999999999999999999999999999999")
        val buffer = ArgumentTokenBuffer(listOf(Token(input.toString(), TokenType.SINGLE_STRING)))

        // THEN
        Assertions.assertTrue(transformer.matches(buffer))
        Assertions.assertEquals(input, transformer.parse(buffer))
    }

    @Test
    fun testColorTransformer() {
        // GIVEN
        val transformer = ColorTransformer

        // WHEN
        val input = Color.BLUE
        val buffer = ArgumentTokenBuffer(listOf(Token(input.toString(), TokenType.SINGLE_STRING)))

        // THEN
        Assertions.assertTrue(transformer.matches(buffer))
        Assertions.assertEquals(input, transformer.parse(buffer))
    }

    @Test
    fun testStringBuilderTransformer() {
        // GIVEN
        val transformer = StringBuilderTransformer

        // WHEN
        val input = StringBuilder("foo")
        val buffer = ArgumentTokenBuffer(listOf(Token(input.toString(), TokenType.SINGLE_STRING)))

        // THEN
        Assertions.assertTrue(transformer.matches(buffer))
        Assertions.assertEquals(input.toString(), transformer.parse(buffer).toString())
    }

    @Test
    fun testTokenTransformer() {
        // GIVEN
        val transformer = TokenTransformer

        // WHEN
        val input = Token("foo", TokenType.SINGLE_STRING)
        val buffer = ArgumentTokenBuffer(listOf(input))

        // THEN
        Assertions.assertTrue(transformer.matches(buffer))
        Assertions.assertEquals(input, transformer.parse(buffer))
    }

    @Test
    fun testURLTransformer() {
        // GIVEN
        val transformer = URLTransformer

        // WHEN
        val input = "https://www.google.com".toURL()
        val buffer = ArgumentTokenBuffer(listOf(Token(input.toString(), TokenType.SINGLE_STRING)))

        // THEN
        Assertions.assertTrue(transformer.matches(buffer))
        Assertions.assertEquals(input, transformer.parse(buffer))
    }

    @Test
    fun testUUIDTransformer() {
        // GIVEN
        val transformer = UUIDTransformer

        // WHEN
        val input = UUID.randomUUID()
        val buffer = ArgumentTokenBuffer(listOf(Token(input.toString(), TokenType.SINGLE_STRING)))

        // THEN
        Assertions.assertTrue(transformer.matches(buffer))
        Assertions.assertEquals(input, transformer.parse(buffer))
    }
}