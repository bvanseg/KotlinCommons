package bvanseg.kcommons.simplenet.packet

import bvanseg.kcommons.projects.Version
import com.github.simplenet.packet.Packet
import org.joml.Vector3dc
import org.joml.Vector3fc
import org.joml.Vector3ic
import java.util.*

/**
 * @author Bright_Spark
 */

fun Packet.putUUID(uuid: UUID) {
    this.putLong(uuid.mostSignificantBits)
    this.putLong(uuid.leastSignificantBits)
}

fun Packet.putVersion(version: Version) {
    putInt(version.major)
    putInt(version.minor)
    putInt(version.patch)
}

/** JOML Helper Functions **/
fun Packet.putVector3i(vector3ic: Vector3ic): Packet = this.putInt(vector3ic.x()).putInt(vector3ic.y()).putInt(vector3ic.z())
fun Packet.putVector3f(vector3fc: Vector3fc): Packet = this.putFloat(vector3fc.x()).putFloat(vector3fc.y()).putFloat(vector3fc.z())
fun Packet.putVector3d(vector3dc: Vector3dc): Packet = this.putDouble(vector3dc.x()).putDouble(vector3dc.y()).putDouble(vector3dc.z())
