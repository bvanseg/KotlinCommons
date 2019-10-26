package bvanseg.kcommons.buffers

import bvanseg.kcommons.compression.decompress
import bvanseg.kcommons.projects.Version
import bvanseg.kcommons.ubjson.UBJ
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