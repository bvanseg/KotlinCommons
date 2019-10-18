package bvanseg.kcommons.joml.vectors

import org.joml.*
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

fun Vector3ic.toVector3f() = Vector3f(this.x().toFloat(), this.y().toFloat(), this.z().toFloat())
fun Vector3ic.toVector3d() = Vector3d(this.x().toDouble(), this.y().toDouble(), this.z().toDouble())

fun Vector3fc.toVector3i() = Vector3i(this.x().toInt(), this.y().toInt(), this.z().toInt())
fun Vector3fc.toVector3d() = Vector3d(this.x().toDouble(), this.y().toDouble(), this.z().toDouble())

fun Vector3dc.toVector3f() = Vector3f(this.x().toFloat(), this.y().toFloat(), this.z().toFloat())
fun Vector3dc.toVector3i() = Vector3i(this.x().toInt(), this.y().toInt(), this.z().toInt())