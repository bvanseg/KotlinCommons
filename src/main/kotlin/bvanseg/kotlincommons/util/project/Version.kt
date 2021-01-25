/*
 * MIT License
 *
 * Copyright (c) 2021 Boston Vanseghi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package bvanseg.kotlincommons.util.project

import bvanseg.kotlincommons.util.HashCodeBuilder

/**
 * A class that adheres to Apache's versioning guidelines. @see <a href="https://commons.apache.org/releases/versioning.html"></a>
 *
 * @author Boston Vanseghi
 * @since 2.0.0
 */
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
        HashCodeBuilder.builder(this::class).append(major).append(minor).append(patch).append(label).hashCode()

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