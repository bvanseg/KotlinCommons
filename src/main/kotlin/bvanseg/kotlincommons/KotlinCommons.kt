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
package bvanseg.kotlincommons

import bvanseg.kotlincommons.io.logging.getLogger
import bvanseg.kotlincommons.util.project.Version
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.net.http.HttpClient
import java.util.Random
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
typealias StringArray = Array<String>

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
typealias Array2D<T> = Array<Array<T>>

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
typealias Array3D<T> = Array<Array<Array<T>>>

/**
 * An object representing KotlinCommons, storing miscellaneous data about the overall project.
 *
 * @author Boston Vanseghi
 * @since 1.0.1
 */
object KotlinCommons {

    /**
     * The version of KotlinCommons.
     */
    val VERSION = Version(2, 7, 0)

    /**
     * A default logger provided by KotlinCommons.
     */
    val KC_LOGGER = getLogger()

    /**
     * A default random instance provided by KotlinCommons for internal or external use.
     */
    val KC_RANDOM = Random()

    /**
     * A default instance of Java 11's HttpClient.
     */
    val KC_HTTP_CLIENT = HttpClient.newHttpClient()

    /**
     * A default instance of fasterXML's Jackson Object Mapper.
     */
    val KC_JACKSON_OBJECT_MAPPER = jacksonObjectMapper()

    val KC_THREAD_ATTRIBUTE_MAP = ConcurrentHashMap<Long, ConcurrentHashMap<String, ThreadLocal<*>>>()
}