package bvanseg.kotlincommons.files

import org.junit.jupiter.api.Test


/**
 * @author Boston Vanseghi
 */
internal class KFileTest {

    @Test
    fun testKFileCreation() {
        val file = KFile("data/stuff/hello_world.txt")
        val dir = KFile("data/stuff/testDirectory")
        assert(file.exists())
        assert(dir.exists())
    }

    @Test
    fun testFileRename() {
        val file = KFile("data/stuff/hello_world.txt")
        assert(file.exists())

        val newFile = file.rename("renamedFile")

        assert(newFile.exists())
    }
}