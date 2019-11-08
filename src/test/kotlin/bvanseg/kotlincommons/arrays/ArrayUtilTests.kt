package bvanseg.kotlincommons.arrays

import bvanseg.kotlincommons.ubjson.UBJ
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * @author Boston Vanseghi
 */
internal class ArrayUtilTests {

	@Test
	fun test2DArrayNulls() {
		// Given
		val array2d = array2dOfNulls<Int>(16)

		// When
		array2d[0][0] = 27

		// Then
		assertEquals(27, array2d[0][0])
	}

	@Test
	fun test3DArrayNulls() {
		// Given
		val array3d = array3dOfNulls<Int>(16)

		// When
		array3d[1][0][3] = 27

		// Then
		assertEquals(27, array3d[1][0][3])
	}

	@Test
	fun test2DArray() {
		// Given
		val array2d = array2dOf(16, 0)

		// When
		array2d[0][0] = 27

		// Then
		assertEquals(27, array2d[0][0])
	}

	@Test
	fun test2DArrayInitializer() {
		// Given
		val array2d = array2dOf(16) { 0 }

		// When
		array2d[0][0] = 27

		// Then
		assertEquals(27, array2d[0][0])
	}

	@Test
	fun test3DArray() {
		// Given
		val array3d = array3dOf(16, 0)

		// When
		array3d[1][0][3] = 27

		// Then
		assertEquals(27, array3d[1][0][3])
	}

	@Test
	fun test3DArrayInitializer() {
		// Given
		val array3d = array3dOf(16) { 0 }

		// When
		array3d[1][0][3] = 27

		// Then
		assertEquals(27, array3d[1][0][3])
	}
}