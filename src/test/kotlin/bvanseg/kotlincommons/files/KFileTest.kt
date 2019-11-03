package bvanseg.kotlincommons.files

import org.junit.jupiter.api.Test


/**
 * @author Boston Vanseghi
 */
internal class KFileTest {

    @Test
    fun testKFileCreation() {
        val file = KFile("data/stuff/hello_world.txt")
        assert(file.exists())
    }

    @Test
    fun testFileRename() {
        var file = KFile("data/stuff/hello_world.txt")
        assert(file.exists())

        file = file.rename("renamedFile")

        assert(file.exists())
    }
}