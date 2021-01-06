package bvanseg.kotlincommons.armada

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class CommandManagerTest {
    @Suppress("unused")
    companion object {
        @JvmStatic
        fun getPrefix(): Stream<Arguments> = Stream.of(
            arguments(null, "!"),
            arguments(1, "?"),
            arguments(2, "!")
        )

        @JvmStatic
        fun stripPrefix_prefix(): Stream<Arguments> = Stream.of(
            arguments("!test arg", "!", "test arg"),
            arguments("pre?test arg", "pre?", "test arg")
        )

        @JvmStatic
        fun stripPrefix_key(): Stream<Arguments> = Stream.of(
            arguments("!test arg", null, "test arg"),
            arguments("?test arg", 1, "test arg"),
            arguments("!test arg", 2, "test arg"),
            arguments("?test arg", null, "?test arg"),
            arguments("?test arg", 2, "?test arg")
        )

        @JvmStatic
        fun splitCommand(): Stream<Arguments> = Stream.of(
            arguments("", "" to ""),
            arguments("test", "test" to ""),
            arguments("test arg", "test" to "arg"),
            arguments("test arg1 arg2", "test" to "arg1 arg2")
        )

        @JvmStatic
        fun extractCommandName(): Stream<Arguments> = Stream.of(
            arguments("!test", null, "test"),
            arguments("!test arg", null, "test"),
            arguments("!test", 1, "!test"),
            arguments("!test arg", 1, "!test"),
            arguments("?test", 1, "test"),
            arguments("?test arg", 1, "test"),
            arguments("!test", 2, "test"),
            arguments("!test arg", 2, "test"),
            arguments("?test", 2, "?test"),
            arguments("?test arg", 2, "?test")
        )
    }

    private lateinit var commandManager: CommandManager<Int>

    @BeforeEach
    fun setUp() {
        commandManager = CommandManager<Int>().apply {
            prefixes[1] = "?"
        }
    }

    @ParameterizedTest
    @MethodSource
    fun getPrefix(id: Int?, expected: String) {
        // Given
        // When
        val prefix = commandManager.getPrefix(id)

        // Then
        assertEquals(expected, prefix)
    }

    @ParameterizedTest
    @MethodSource
    fun stripPrefix_prefix(command: String, prefix: String, expected: String) {
        // Given
        // When
        val result = commandManager.stripPrefix(command, prefix)

        // Then
        assertEquals(expected, result)
    }

    @ParameterizedTest
    @MethodSource
    fun stripPrefix_key(command: String, key: Int?, expected: String) {
        // Given
        // When
        val result = commandManager.stripPrefix(command, key)

        // Then
        assertEquals(expected, result)
    }

    @ParameterizedTest
    @MethodSource
    fun splitCommand(command: String, expected: Pair<String, String>) {
        // Given
        // When
        val result = commandManager.splitCommand(command)

        // Then
        assertEquals(expected, result)
    }

    @ParameterizedTest
    @MethodSource
    fun extractCommandName(command: String, key: Int?, expected: String) {
        // Given
        // When
        val result = commandManager.extractCommandName(command, key)

        // Then
        assertEquals(expected, result)
    }
}
