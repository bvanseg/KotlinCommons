/*
 * MIT License
 *
 * Copyright (c) 2020 Boston Vanseghi
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
package bvanseg.kotlincommons.arrays

/**
 * Creates a 2D array of the given type [T].
 *
 * @param size - The size of all nested and top-level arrays.
 * @param defaultValue - The default value to populate every array index with.
 *
 * @author Boston Vanseghi
 * @since 2.0.2
 */
inline fun <reified T> array2dOf(size: Int, defaultValue: T): Array<Array<T>> = Array(size) {
    Array(size) {
        defaultValue
    }
}

/**
 * Creates a 2D array of the given type [T].
 *
 * @param size - The size of all nested and top-level arrays.
 * @param defaultValue - The default value to populate every array index with.
 *
 * @author Boston Vanseghi
 * @since 2.0.2
 */
inline fun <reified T> array2dOf(size: Int, noinline defaultValue: (Int) -> T): Array<Array<T>> = Array(size) {
    Array(size, defaultValue)
}

/**
 * Creates a 2D array of the given type [T] where each element is nullable.
 *
 * @param size - The maximum size of the entire construct (all arrays).
 *
 * @author Boston Vanseghi
 * @since 2.0.2
 */
inline fun <reified T> array2dOfNulls(size: Int): Array<Array<T?>> = Array(size) {
    arrayOfNulls<T?>(size)
}

/**
 * Creates a 3D array of the given type [T].
 *
 * @param size - The size of all nested and top-level arrays.
 * @param defaultValue - The default value to populate every array index with.
 *
 * @author Boston Vanseghi
 * @since 2.0.2
 */
inline fun <reified T> array3dOf(size: Int, defaultValue: T): Array<Array<Array<T>>> = Array(size) {
    Array(size) {
        Array(size) {
            defaultValue
        }
    }
}

/**
 * Creates a 3D array of the given type [T].
 *
 * @param size - The size of all nested and top-level arrays.
 * @param defaultValue - The default value to populate every array index with.
 *
 * @author Boston Vanseghi
 * @since 2.0.2
 */
inline fun <reified T> array3dOf(size: Int, noinline defaultValue: (Int) -> T): Array<Array<Array<T>>> = Array(size) {
    Array(size) {
        Array(size, defaultValue)
    }
}

/**
 * Creates a 3D array of the given type [T] where each element is nullable.
 *
 * @param size - The maximum size of the entire construct (all arrays).
 *
 * @author Boston Vanseghi
 * @since 2.0.2
 */
inline fun <reified T> array3dOfNulls(size: Int): Array<Array<Array<T?>>> = Array(size) {
    Array(size) {
        arrayOfNulls<T?>(size)
    }
}