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
package bvanseg.kotlincommons.io.buffer

import bvanseg.kotlincommons.project.Version
import java.nio.ByteBuffer
import java.util.UUID

/**
 * Reads two [Byte]s from the [ByteBuffer].
 *
 * @author Boston Vanseghi
 * @since 2.7.0
 */
@Deprecated("Deprecated due to implementation being too specific.", ReplaceWith("getBytes(this.short.toInt())"))
fun ByteBuffer.getBytes(): ByteArray = getBytes(this.short.toInt())

/**
 * Reads a given [size] of [Byte]s from the [ByteBuffer].
 *
 * @param size The number of [Byte]s to read from the [ByteBuffer].
 *
 * @author Boston Vanseghi
 * @since 2.7.0
 */
fun ByteBuffer.getBytes(size: Int): ByteArray {
    val bytes = ByteArray(size)
    if (this.remaining() > 0) {
        this.get(bytes)
    }
    return bytes
}

/**
 * Reads the next two [Long]s into a [UUID] from the [ByteBuffer].
 *
 * @author Boston Vanseghi
 * @since 2.7.0
 */
fun ByteBuffer.getUUID(): UUID = UUID(long, long)

/**
 * Gets a [String] object from the [ByteBuffer].
 *
 * @author Boston Vanseghi
 * @since 2.7.0
 */
fun ByteBuffer.getString(): String {
    val stringLength = this.short.toInt()
    return String(this.getBytes(stringLength))
}

/**
 * Reads the next three [Int]s in the [ByteBuffer] and a [String] following them, returning a [Version] object.
 *
 * @author Boston Vanseghi
 * @since 2.7.0
 */
fun ByteBuffer.getVersion(): Version = Version(int, int, int, getString())

/**
 * Reads the next [Byte] available in the buffer and converts it to a [Boolean].
 *
 * @author Boston Vanseghi
 * @since 2.7.0
 */
fun ByteBuffer.getBoolean(): Boolean = get().toInt() == 1