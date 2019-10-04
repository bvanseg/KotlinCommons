package bvanseg.kcommons.buffers

import bvanseg.kcommons.projects.Version
import java.nio.ByteBuffer
import java.util.*

fun ByteBuffer.getBytes(): ByteArray = getBytes(this.short.toInt())

fun ByteBuffer.getBytes(num: Int): ByteArray {
    val bytes = ByteArray(num)
    this.get(bytes)
    return bytes
}

fun ByteBuffer.getUUID(): UUID = UUID(long, long)

fun ByteBuffer.getString(): String = String(this.getBytes())

fun ByteBuffer.getVersion(): Version = Version(int, int, int)

fun ByteBuffer.getBoolean(): Boolean  = get().toInt() == 1