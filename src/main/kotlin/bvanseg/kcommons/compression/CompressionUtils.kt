package bvanseg.kcommons.compression

import java.io.ByteArrayOutputStream
import java.util.zip.Deflater
import java.util.zip.Inflater

fun compress(data: ByteArray, bufferSize: Int = 20000): ByteArray {
    val deflater = Deflater()
    deflater.setInput(data)
    val outputStream = ByteArrayOutputStream(data.size)
    deflater.finish()
    val buffer = ByteArray(bufferSize)
    while (!deflater.finished()) {
        val count = deflater.deflate(buffer) // returns the generated code... index
        outputStream.write(buffer, 0, count)
    }
    outputStream.close()
    return outputStream.toByteArray()
}

fun decompress(data: ByteArray, bufferSize: Int = 20000): ByteArray {
    val inflater = Inflater()
    inflater.setInput(data)
    val outputStream = ByteArrayOutputStream(data.size)
    val buffer = ByteArray(bufferSize)
    while (!inflater.finished()) {
        val count = inflater.inflate(buffer)
        outputStream.write(buffer, 0, count)
    }
    outputStream.close()
    return outputStream.toByteArray()
}
