import bvanseg.kotlincommons.projects.Version
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class VersionTests {

    @Test
    fun versionTesting() {
        val v001 = Version("0.0.1")
        val v010 = Version("0.1.0")
        val v100 = Version("1.0.0")

        val v010a = Version("0.1.0-alpha")
        val v010b = Version("0.1.0-beta")
        val v010r = Version("0.1.0-release")

        assertEquals(-1, v001.compareTo(v010))
        assertEquals(-1, v010.compareTo(v100))

        assertEquals(1, v100.compareTo(v010))
        assertEquals(1, v010.compareTo(v001))

        assertEquals(0, v010a.compareTo(v010b))
        assertEquals(0, v010b.compareTo(v010a))

        assertEquals(0, v010r.compareTo(v010a))
        assertEquals(0, v010r.compareTo(v010b))

        assertEquals(0, v010a.compareTo(v010r))
        assertEquals(0, v010b.compareTo(v010r))

        assertEquals(0, v010.compareTo(v010))
    }
}