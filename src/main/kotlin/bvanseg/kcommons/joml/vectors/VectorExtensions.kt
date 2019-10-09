package bvanseg.kcommons.joml.vectors

import org.joml.Vector3dc
import org.joml.Vector3fc
import org.joml.Vector3ic
import kotlin.math.abs


/**
 * Checks if the [otherVector] is within an area within the [radius] of this vector
 */
fun Vector3ic.isWithinRadius(otherVector: Vector3ic, radius: Int = 1): Boolean =
    abs(x() - otherVector.x()) <= radius && abs(y() - otherVector.y()) <= radius && abs(z() - otherVector.z()) <= radius

fun Vector3dc.isWithinRadius(otherVector: Vector3ic, radius: Double = 1.0): Boolean =
    abs(x() - otherVector.x()) <= radius && abs(y() - otherVector.y()) <= radius && abs(z() - otherVector.z()) <= radius

fun Vector3fc.isWithinRadius(otherVector: Vector3ic, radius: Float = 1f): Boolean =
    abs(x() - otherVector.x()) <= radius && abs(y() - otherVector.y()) <= radius && abs(z() - otherVector.z()) <= radius
