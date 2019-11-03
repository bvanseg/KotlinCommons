package bvanseg.kotlincommons.compression

import bvanseg.kotlincommons.ubjson.UBJ
import org.junit.jupiter.api.Test

/**
 * @author Boston Vanseghi
 */
internal class CompressionTest {

	@Test
	fun test() {
		val ubj = UBJ()
		val data = ByteArray(10000)
		val comp = compress(data)

		ubj.setByteArray("bytes", *comp)
		ubj.getByteArray("bytes")?.let {
			val decomp = decompress(it)
			assert(decomp.contentEquals(data))
		}
	}
}