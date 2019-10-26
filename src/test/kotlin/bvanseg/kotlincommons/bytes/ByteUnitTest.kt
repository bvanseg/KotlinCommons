package bvanseg.kotlincommons.bytes

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

/**
 * @author bright_spark
 */
internal class ByteUnitTest {
    @ParameterizedTest(name = "{0}{1} to {3}{2}")
    @CsvSource(
        "1,             B,  B,  1",
        "1024,          B,  KB, 1",
        "1048576,       B,  MB, 1",
        "1073741824,    B,  GB, 1",
        "1099511627776, B,  TB, 1",
        "1,             KB, B,  1024",
        "1,             KB, KB, 1",
        "1024,          KB, MB, 1",
        "1048576,       KB, GB, 1",
        "1073741824,    KB, TB, 1",
        "1,             MB, B,  1048576",
        "1,             MB, KB, 1024",
        "1,             MB, MB, 1",
        "1024,          MB, GB, 1",
        "1048576,       MB, TB, 1",
        "1,             GB, B,  1073741824",
        "1,             GB, KB, 1048576",
        "1,             GB, MB, 1024",
        "1,             GB, GB, 1",
        "1024,          GB, TB, 1",
        "1,             TB, B,  1099511627776",
        "1,             TB, KB, 1073741824",
        "1,             TB, MB, 1048576",
        "1,             TB, GB, 1024",
        "1,             TB, TB, 1",
        "1,             GB, B,  1073741824",
        "1,             GB, KB, 1048576",
        "1,             GB, MB, 1024",
        "1024,          GB, TB, 1",
        "1,             MB, B,  1048576",
        "1,             MB, KB, 1024",
        "1024,          MB, GB, 1",
        "1048576,       MB, TB, 1",
        "1,             KB, B,  1024",
        "1024,          KB, MB, 1",
        "1048576,       KB, GB, 1",
        "1073741824,    KB, TB, 1"
    )
    fun to(inputVal: Long, inputUnit: ByteUnit, outputUnit: ByteUnit, expectedVal: Long) {
        // Given
        // When
        val result = inputUnit.to(inputVal, outputUnit)

        // Then
        Assertions.assertEquals(expectedVal, result)
    }
}
