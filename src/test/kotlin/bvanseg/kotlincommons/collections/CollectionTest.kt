package bvanseg.kotlincommons.collections

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

/**
 * @author Boston Vanseghi
 */
internal class CollectionTest {

	@Test
	fun testMapReversal() {
        val hashMap: MutableMap<Int, String> = hashMapOf()
        hashMap[0] = "foo"
        hashMap[1] = "bar"
        hashMap[2] = "foobar"

        val reverse = hashMap.reverse()

        assert(reverse["foo"] == 0)
        assert(reverse["bar"] == 1)
        assert(reverse["foobar"] == 2)
	}
}