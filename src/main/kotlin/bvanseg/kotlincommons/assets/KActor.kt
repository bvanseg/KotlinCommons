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

/**
 * Represents some actor/entity that belongs to a [domain] and has a specific [location].
 *
 * @author Boston Vanseghi
 * @since 2.1.1
 */
class KActor(override var domain: String, override var location: String): KResource {

    constructor(name: String): this(defaultDomain, name)

    override val path: String
        get() = "$defaultRoot$domain/$location"

    override fun toResourceMode(): KResourceLocation = KResourceLocation(domain, location)
    override fun toActorMode(shouldClose: Boolean): KActor = this

    override fun toString(): String = Textualizer.builder(this)
        .append("domain", domain)
        .append("location", location)
        .append("path", path)
        .toString()

    override fun hashCode(): Int = Hasher(this).append(path).hashCode()
    override fun equals(other: Any?): Boolean = other is KActor && this.domain == other.domain && this.location == other.location
}