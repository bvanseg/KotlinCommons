package bvanseg.kotlincommons.time

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.Duration
import java.util.stream.Stream

/**
 * @author bright_spark
 */
internal class TimeExtensionsKtTest {
	companion object {
		@JvmStatic
		fun durationFormat() = Stream.of(
			arguments(Duration.ofHours(24), false, "24 hours"),
			arguments(Duration.ofMinutes(60), false, "1 hour"),
			arguments(Duration.ofMinutes(59), false, "59 minutes"),
			arguments(Duration.ofSeconds(60), false, "1 minute"),
			arguments(Duration.ofSeconds(59), false, "59 seconds"),
			arguments(Duration.ofSeconds(1), false, "1 second"),
			arguments(Duration.ofSeconds(9030), true, "2 hours, 30 minutes and 30 seconds"),
			arguments(Duration.ofMillis(100), true, "100 milliseconds"),
			arguments(Duration.ofMillis(1), true, "1 millisecond"),
			arguments(Duration.ofMillis(100), false, "0 seconds"),
			arguments(Duration.ofMillis(1001), false, "1 second"),
			arguments(Duration.ofMillis(2001), true, "2 seconds and 1 millisecond"),
			arguments(Duration.ofMillis(0), true, "0 milliseconds")
		)
	}

	@ParameterizedTest
	@MethodSource
	fun durationFormat(duration: Duration, millis: Boolean, expected: String) {
		// Given
		// When
		val result = duration.format(millis)

		// Then
		assertEquals(expected, result)
	}
}