package bvanseg.kotlincommons.compression

import java.io.ByteArrayOutputStream
import java.util.zip.Deflater
import java.util.zip.Inflater

/**
 * Compresses a [ByteArray] under standard compression.
 *
 * @param bufferSize - The maximum size of the compressed [ByteArray]
 *
 * @return The compressed [ByteArray]
 *
 * @author Boston Vanseghi
 * @since 1.0.0
 */
fun ByteArray.compress(bufferSize: Int = 20000): ByteArray {
    val deflater = Deflater()
    deflater.setInput(this)
    val outputStream = ByteArrayOutputStream(this.size)
    deflater.finish()
    val buffer = ByteArray(bufferSize)
    while (!deflater.finished()) {
        val count = deflater.deflate(buffer)
        outputStream.write(buffer, 0, count)
    }
    outputStream.close()
    return outputStream.toByteArray()
}

/**
 * Decompresses a [ByteArray] under standard decompression.
 *
 * @param bufferSize - The maximum size of the decompressed [ByteArray]
 *
 * @return The decompressed [ByteArray]
 *
 * @author Boston Vanseghi
 * @since 1.0.0
 */
fun ByteArray.decompress(bufferSize: Int = 20000): ByteArray {
    val inflater = Inflater()
    inflater.setInput(this)
    val outputStream = ByteArrayOutputStream(this.size)
    val buffer = ByteArray(bufferSize)
    while (!inflater.finished()) {
        val count = inflater.inflate(buffer)
        outputStream.write(buffer, 0, count)
    }
    outputStream.close()
    return outputStream.toByteArray()
}