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