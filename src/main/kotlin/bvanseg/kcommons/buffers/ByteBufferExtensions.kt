package bvanseg.kcommons.buffers

import bvanseg.kcommons.projects.Version
import org.joml.*
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

/** JOML Helper Functions **/
fun ByteBuffer.getVector3i(): Vector3ic = Vector3i(this.int, this.int, this.int)
fun ByteBuffer.getVector3f(): Vector3fc = Vector3f(this.float, this.float, this.float)
fun ByteBuffer.getVector3d(): Vector3dc = Vector3d(this.double, this.double, this.double)