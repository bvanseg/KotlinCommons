package bvanseg.kcommons.compression

import bvanseg.kcommons.ubjson.UBJ
import org.junit.jupiter.api.Test

/**
 * @author Boston Vanseghi
 */
internal class CompressionTest {

	@Test
	fun test() {
		val ubj = UBJ()

		val data = ByteArray(10000)
		println(data.size)
		val comp = compress(data)
		println(comp.size)

		ubj.setByteArray("bytes", *comp)

		ubj.getByteArray("bytes")?.let {

			println(it.size)
			val decomp = decompress(it)
			println(decomp.size)

			assert(decomp.contentEquals(data))
		}
	}
}