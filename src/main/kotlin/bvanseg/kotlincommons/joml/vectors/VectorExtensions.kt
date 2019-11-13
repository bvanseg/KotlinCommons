@file:Suppress("EXTENSION_SHADOWED_BY_MEMBER")

package bvanseg.kotlincommons.joml.vectors

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

fun Vector3i.div(x: Int, y: Int, z: Int): Vector3i = this.set(this.x / x, this.y / y, this.z / z)

operator fun Vector3i.plus(vector3i: Vector3i): Vector3i = this.add(vector3i.x, vector3i.y, vector3i.z)
operator fun Vector3i.minus(vector3i: Vector3i): Vector3i = this.sub(vector3i.x, vector3i.y, vector3i.z)
operator fun Vector3i.times(vector3i: Vector3i): Vector3i = this.mul(vector3i.x, vector3i.y, vector3i.z)
operator fun Vector3i.div(vector3i: Vector3i): Vector3i = this.div(vector3i.x, vector3i.y, vector3i.z)

operator fun Vector3f.plus(vector3f: Vector3f): Vector3f = this.add(vector3f.x, vector3f.y, vector3f.z)
operator fun Vector3f.minus(vector3f: Vector3f): Vector3f = this.sub(vector3f.x, vector3f.y, vector3f.z)
operator fun Vector3f.times(vector3f: Vector3f): Vector3f = this.mul(vector3f.x, vector3f.y, vector3f.z)
operator fun Vector3f.div(vector3f: Vector3f): Vector3f = this.div(vector3f.x, vector3f.y, vector3f.z)

operator fun Vector3d.plus(vector3d: Vector3d): Vector3d = this.add(vector3d.x, vector3d.y, vector3d.z)
operator fun Vector3d.minus(vector3d: Vector3d): Vector3d = this.sub(vector3d.x, vector3d.y, vector3d.z)
operator fun Vector3d.times(vector3d: Vector3d): Vector3d = this.mul(vector3d.x, vector3d.y, vector3d.z)
operator fun Vector3d.div(vector3d: Vector3d): Vector3d = this.div(vector3d.x, vector3d.y, vector3d.z)