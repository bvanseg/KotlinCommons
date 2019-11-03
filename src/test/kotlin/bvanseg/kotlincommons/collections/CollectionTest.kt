package bvanseg.kotlincommons.collections

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * @author Boston Vanseghi
 */
internal class CollectionTest {

	@Test
	fun mapReversal() {
        val hashMap: MutableMap<Int, String> = hashMapOf()
        hashMap[0] = "foo"
        hashMap[1] = "bar"
        hashMap[2] = "foobar"

        val reverse = hashMap.reversedMutable()
        reverse["rada"] = 3

        assertEquals(0, reverse["foo"])
        assertEquals(1, reverse["bar"])
        assertEquals(2, reverse["foobar"])
	}

    @Test
    fun mapReversalEditing() {
        val hashMap: MutableMap<Int, String> = hashMapOf()

        val reverse = hashMap.reversedMutable()
        reverse["rada"] = 3

        assertEquals(3, reverse["rada"])
    }

    @Test
    fun originalMapEdit() {
        val hashMap: MutableMap<Int, String> = hashMapOf()

        val reverse = hashMap.reversedMutable()
        reverse["rada"] = 3

        assertEquals(hashMap[3], "rada")
    }
}