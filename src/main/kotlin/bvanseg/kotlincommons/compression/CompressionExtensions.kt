/*
 * MIT License
 *
 * Copyright (c) 2020 Boston Vanseghi
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