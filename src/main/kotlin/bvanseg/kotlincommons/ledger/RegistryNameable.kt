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
package bvanseg.kotlincommons.ledger

import bvanseg.kotlincommons.assets.KResourceLocation

/**
 * @author bright_spark
 * @since 2.1.0
 */
abstract class RegistryNameable() {
    /**
     * @return The registry name for this object
     */
    lateinit var registryName: KResourceLocation
        private set

    constructor(name: String) : this() {
        this.registryName = KResourceLocation(name)
    }

    constructor(resourceLocation: KResourceLocation) : this() {
        this.registryName = resourceLocation
    }

    //TODO: Rework this to play nice with the new KResourceLocation
//    /**
//     * Sets the domain in the [registryName] if not already set
//     *
//     * @param domain The domain to be set in the registry name
//     */
//    fun setDomain(domain: String) {
//        if (this.registryName.domain == null)
//            this.registryName.domain = domain
//    }
}

