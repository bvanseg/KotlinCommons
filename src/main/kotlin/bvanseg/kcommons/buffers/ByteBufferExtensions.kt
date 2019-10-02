package bvanseg.kcommons.buffers

import bvanseg.kcommons.projects.Version
import java.nio.ByteBuffer
import java.util.*

fun ByteBuffer.getBytes(buffer: ByteBuffer): ByteArray = this.getBytes(this, this.short.toInt())

fun ByteBuffer.getBytes(buffer: ByteBuffer, num: Int): ByteArray {
    val bytes = ByteArray(num)
    buffer.get(bytes)
    return bytes
}

fun ByteBuffer.getUUID(): UUID = UUID(long, long)

fun ByteBuffer.getString(): String = String(this.getBytes(this))

fun ByteBuffer.getVersion(): Version = Version(int, int, int)

fun ByteBuffer.getBoolean(): Boolean  = get().toInt() == 1