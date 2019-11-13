package bvanseg.kotlincommons.files

import java.io.File
import java.io.IOException
import java.io.FileOutputStream
import java.io.FileInputStream
import java.nio.channels.FileChannel
import java.nio.file.Files

/**
 * Renames a [File] to the given [name]. Extensions and directories are handled automatically.
 *
 * @author Boston Vanseghi
 * @since 2.0.1
 */
fun File.rename(name: String): File {
    val newFile = File(this.parent + "/$name." + this.extension)

    if(newFile.exists())
        throw IOException("File already exists!")

    this.renameTo(newFile)

    return newFile
}
/**
 * Copies the current [File] to a new [File] with the given [destination].
 *
 * @author Boston Vanseghi
 * @since 2.0.1
 */
fun File.copyTo(destination: File) {
    if (!destination.exists())
        Files.createFile(destination.toPath())

    val source: FileChannel? = FileInputStream(this).channel
    val dest: FileChannel? = FileOutputStream(destination).channel

    source!!.use {
        dest!!.use {
            dest.transferFrom(source, 0, source.size())
        }
    }
}

fun File.isDirectoryPath(): Boolean = this.name.lastIndexOf('.') == -1