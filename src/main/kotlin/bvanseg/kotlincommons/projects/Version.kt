package bvanseg.kotlincommons.projects

import bvanseg.kotlincommons.numbers.Hasher

class Version : Comparable<Version> {

    val major: Int
    val minor: Int
    val patch: Int
    val label: String

    constructor(version: String) {
        val data = version.split(".", "-")
        major = data[0].toInt()
        minor = data[1].toInt()
        patch = data[2].toInt()

        label = if (data.size > 3)
            data[3]
        else
            ""
    }

    constructor (major: Int, minor: Int, patch: Int, label: String = "") {
        this.major = major
        this.minor = minor
        this.patch = patch
        this.label = label
    }

    override fun equals(other: Any?): Boolean = other is Version
            && this.major == other.major
            && this.minor == other.minor
            && this.patch == other.patch
            && this.label == other.label

    override fun toString(): String = "$major.$minor.$patch" + if (label != "") "-$label" else ""
    override fun hashCode(): Int =
        Hasher().append(major).append(minor).append(patch).append(label).hashCode()

    override fun compareTo(other: Version): Int {
        return if (this.major > other.major) {
            1
        } else if (this.major < other.major) {
            -1
        } else if (this.major == other.major && this.minor > other.minor) {
            1
        } else if (this.major == other.major && this.minor < other.minor) {
            -1
        } else if (this.major == other.major && this.minor == other.minor && this.patch > other.patch) {
            1
        } else if (this.major == other.major && this.minor == other.minor && this.patch < other.patch) {
            -1
        } else 0
    }
}