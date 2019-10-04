package bvanseg.kcommons.simplenet.packet

import bvanseg.kcommons.projects.Version
import com.github.simplenet.packet.Packet
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