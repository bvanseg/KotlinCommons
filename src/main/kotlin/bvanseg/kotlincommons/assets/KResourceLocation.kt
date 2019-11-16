/*
 * MIT License
 *
 * Copyright (c) 2019 Boston Vanseghi
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
package bvanseg.kotlincommons.assets

import bvanseg.kotlincommons.assets.KResource.Companion.defaultDomain
import bvanseg.kotlincommons.assets.KResource.Companion.defaultRoot
import bvanseg.kotlincommons.numbers.Hasher
import bvanseg.kotlincommons.string.Textualizer
import java.io.File
import java.io.InputStream

/**
 * Represents a resource either in the classpath or on disk.
 *
 * @author Boston Vanseghi
 * @author Ocelot5836
 * @since 2.1.1
 */
class KResourceLocation: KResource, AutoCloseable {

    override var domain: String = defaultDomain
        private set
    override var location: String = ""
        private set

    override val path: String
        get() = "$defaultRoot$domain/$location"

    constructor(domain: String, location: String) {
        this.domain = domain
        this.location = location
    }

    constructor(parent: KResourceLocation) {
        this.domain = parent.domain
        this.location = parent.location
    }

    constructor(location: String) {
        val resourceLocationRaw = location.split(":".toRegex(), 2).toTypedArray()
        if (resourceLocationRaw.size > 1) {
            this.domain = resourceLocationRaw[0]
            this.location = resourceLocationRaw[1]
        } else {
            this.domain = defaultDomain
            this.location = location
        }
    }

    private val resource by lazy { Resources.scan.getResourcesWithPath(path).first() }

    val inputStream: InputStream by lazy { resource.open() }
    val file: File by lazy { resource.classpathElementFile }

    override fun toResourceMode(): KResourceLocation = this

    override fun toActorMode(shouldClose: Boolean): KActor {
        if(shouldClose) close()
        return KActor(domain, location)
    }

    override fun close() = inputStream.close()

    override fun toString(): String = Textualizer.builder(this)
        .append("domain", domain)
        .append("location", location)
        .append("path", path)
        .toString()

    override fun hashCode(): Int = Hasher(this).append(path).hashCode()
    override fun equals(other: Any?): Boolean = other is KResourceLocation && this.domain == other.domain && this.location == other.location
}