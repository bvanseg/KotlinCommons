package bvanseg.kotlincommons.files

import java.io.File
import java.net.URI

/**
 * Lightweight wrapper around a [File] that automatically creates directories and the
 * actual [File] on instantiation (if the file does not already exist).
 *
 * @author Boston Vanseghi
 * @since 2.0.1
 */
class KFile: File {

    var fileType: KFileType = KFileType.AMBIGUOUS

    constructor(path: String, type: KFileType = KFileType.AMBIGUOUS): super(path) {
        this.fileType = type
    }
    constructor(uri: URI, type: KFileType = KFileType.AMBIGUOUS): super(uri) {
        this.fileType = type
    }
    constructor(parent: String, child: String, type: KFileType = KFileType.AMBIGUOUS): super(parent, child) {
        this.fileType = type
    }
    constructor(parent: File, path: String, type: KFileType = KFileType.AMBIGUOUS): super(parent, path) {
        this.fileType = type
    }

    init {
        if(!exists())
            mk()
    }

    fun mk() {
        parentFile.mkdirs()
        when (fileType) {
            KFileType.DIRECTORY -> mkdir()
            KFileType.FILE -> createNewFile()
            KFileType.AMBIGUOUS -> {
                if(name.contains(".")) createNewFile()
                else mkdir()
            }
        }
    }

    fun rename(newName: String): KFile {
        val newFile = KFile(this.parent + "/$newName." + this.extension)
        this.copyTo(newFile)
        this.delete()
        return newFile
    }

    /**
     * Used to represent what type of file this [KFile] is.
     *
     * @author Boston Vanseghi
     * @since 2.0.3
     */
    enum class KFileType {
        FILE,
        DIRECTORY,
        AMBIGUOUS
    }
}