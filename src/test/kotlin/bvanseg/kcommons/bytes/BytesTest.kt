package bvanseg.kcommons.bytes

import org.junit.jupiter.api.Test

/**
 * @author Boston Vanseghi
 */
internal class BytesTest {

	@Test
	fun test() {
		println(ByteUnit.B.getBytes())
		println(ByteUnit.B.getBytes(900))
		println(ByteUnit.KB.getBytes(900))

		println(ByteUnit.MB.to(ByteUnit.KB))
		println(ByteUnit.KB.to(ByteUnit.MB))
		println(ByteUnit.TB.to(ByteUnit.B))
		println(ByteUnit.TB.to(ByteUnit.GB))
		println(ByteUnit.TB.to(ByteUnit.GB, 2))
	}
}