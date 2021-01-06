/*
 * MIT License
 *
 * Copyright (c) 2021 Boston Vanseghi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package bvanseg.kotlincommons.file

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