import bvanseg.kotlincommons.assets.KActor
import bvanseg.kotlincommons.assets.KResourceLocation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ResLocTest {

    @Test
    fun testFileHandle() {
        // Given
        val res = KResourceLocation("res", "js.png")

        // When
        // Then
        assertTrue(res.file.exists())
    }

    @Test
    fun testResLocConversion() {
        // Given
        val res = KResourceLocation("res", "js.png")

        // When
        val actor = res.toActorMode(true)

        // Then
        assertEquals("assets/res/js.png", actor.path)
    }

    @Test
    fun testKActors() {
        // Given
        val actor = KActor("dom", "foo")

        // When
        // Then
        assertEquals("dom", actor.domain)
        assertEquals("foo", actor.location)
        assertEquals("assets/dom/foo", actor.path)
    }
}