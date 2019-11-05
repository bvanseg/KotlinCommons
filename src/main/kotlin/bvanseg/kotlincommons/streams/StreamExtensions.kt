package bvanseg.kotlincommons.streams

import java.util.stream.Stream

/**
 * Creates a [Stream] of this [Array]
 *
 * @author bright_spark
 * @since 2.0.1
 */
fun <T> Array<T>.stream(): Stream<T> = Stream.of<T>(*this)