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
package bvanseg.kotlincommons.buffers

import bvanseg.kotlincommons.compression.decompress
import bvanseg.kotlincommons.projects.Version
import bvanseg.kotlincommons.ubjson.UBJ
import com.devsmart.ubjson.UBObject
import com.devsmart.ubjson.UBReader
import org.joml.Vector3d
import org.joml.Vector3f
import org.joml.Vector3i
import java.io.ByteArrayInputStream
import java.io.IOException
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

/** JOML Helper Functions **/
fun ByteBuffer.getVector3i(): Vector3i = Vector3i(this.int, this.int, this.int)

fun ByteBuffer.getVector3f(): Vector3f = Vector3f(this.float, this.float, this.float)
fun ByteBuffer.getVector3d(): Vector3d = Vector3d(this.double, this.double, this.double)

/** UBJ Helper Functions **/
fun ByteBuffer.getUBJ(): UBJ? = getUBObject()?.let { UBJ(it) }

fun ByteBuffer.getUBObject(): UBObject? {
    val bytes = decompress(this.getBytes())
    try {
        UBReader(ByteArrayInputStream(bytes)).use { reader -> return reader.read().asObject() }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}