import bvanseg.kcommons.projects.Version
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

        println(v001.compareTo(v010))
        println(v010.compareTo(v100))

        println(v100.compareTo(v010))
        println(v010.compareTo(v001))

        println(v010.compareTo(v010))

        println(v010a.compareTo(v010b))
        println(v010b.compareTo(v010a))

        println(v010r.compareTo(v010a))
        println(v010r.compareTo(v010b))

        println(v010a.compareTo(v010r))
        println(v010b.compareTo(v010r))
    }
}