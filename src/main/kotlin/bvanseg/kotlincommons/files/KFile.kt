/*
 * MIT License
 *
 * Copyright (c) 2019 Boston Vanseghi
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
package bvanseg.kotlincommons.files

import bvanseg.kotlincommons.booleans.ifFalse
import bvanseg.kotlincommons.booleans.ifTrue
import java.io.File
import java.net.URI
import java.nio.file.Files

/**
 * Lightweight wrapper around a [File] that automatically creates directories and the
 * actual [File] on instantiation (if the file does not already exist).
 *
 * @author Boston Vanseghi
 * @since 2.0.1
 */
class KFile: File {

    constructor(path: String): super(path)
    constructor(uri: URI): super(uri)
    constructor(parent: String, child: String): super(parent, child)
    constructor(parent: File, path: String): super(parent, path)

    init {
        if(!exists())
            mk()
    }

    fun mk() {
        parentFile?.let {
            if(!parentFile.exists())
                if(it.isDirectoryPath())  Files.createDirectory(it.toPath()) else Files.createFile(it.toPath())
        }
        this.exists().ifFalse {
            if(isDirectoryPath()) {
                Files.createDirectory(this.toPath())
            } else {
                Files.createFile(this.toPath())
            }
        }
    }

    fun rename(newName: String): KFile {
        val newFile = KFile(this.parent + "/$newName." + this.extension)
        this.copyTo(newFile)
        Files.deleteIfExists(this.toPath())
        return newFile
    }
}