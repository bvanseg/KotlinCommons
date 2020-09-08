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
package bvanseg.kotlincommons.buffers

import bvanseg.kotlincommons.projects.Version
import java.nio.ByteBuffer
import java.util.*

fun ByteBuffer.getBytes(): ByteArray = getBytes(this.short.toInt())

fun ByteBuffer.getBytes(num: Int): ByteArray {
    val bytes = ByteArray(num)
    if (this.remaining() > 0)
        this.get(bytes)
    return bytes
}

fun ByteBuffer.getUUID(): UUID = UUID(long, long)

fun ByteBuffer.getString(): String = String(this.getBytes())

fun ByteBuffer.getVersion(): Version = Version(int, int, int, getString())

fun ByteBuffer.getBoolean(): Boolean = get().toInt() == 1