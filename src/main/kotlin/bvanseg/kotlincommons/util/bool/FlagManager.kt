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
package bvanseg.kotlincommons.util.bool

import bvanseg.kotlincommons.grouping.collection.DualHashMap
import bvanseg.kotlincommons.lang.checks.Checks
import java.math.BigInteger
import java.util.EnumSet

/**
 * A utility class that uses bitwise operators to read and write an [EnumSet] to and from a [Long]. Significantly useful
 * for compressing enum sets into a single long value.
 *
 * @author Boston Vanseghi
 * @since 2.2.4
 */
class FlagManager<T : Enum<T>>(private val enumClass: Class<T>) {

    private val offsetsToEnumValues = DualHashMap<Long, T>()

    /**
     * Current offset in bits.
     */
    private var currentOffset: Long = 0

    companion object {
        /**
         * Creates a [FlagManager] on the given [Enum] type. Automatically invokes [register] on every [Enum] value within
         * the enum type.
         */
        inline fun <reified T : Enum<T>> createManager(): FlagManager<T> {
            val manager = FlagManager(T::class.java)

            val values = enumValues<T>()

            for (value in values)
                manager.register(value)

            return manager
        }
    }

    /**
     * Registers a given [Enum] value into the [FlagManager]. If an [Enum] is not registered to the [FlagManager], it is
     * not accessible via offset fetching. Invoking [createManager] on an [Enum] type will call this function for all
     * corresponding values of that [Enum] type.
     */
    fun register(value: T) {
        offsetsToEnumValues[currentOffset++] = value
    }

    /**
     * Returns the offset as a [Long] for the given [Enum] value.
     *
     * @param value The [Enum] value to get the offset for.
     *
     * @return The offset as a [Long] for the given [Enum] type.
     */
    fun getOffsetFor(value: Enum<T>): Long = offsetsToEnumValues.reverse()[value]!!

    /**
     * Gets the [Enum] value represented by the given offset.
     *
     * @param offset The [Long] value that maps to the desired [Enum] type.
     *
     * @return An [Enum] value from its corresponding offset.
     */
    fun getValue(offset: Long): Enum<T>? {
        Checks.isWholeNumber(offset, "offset")

        return offsetsToEnumValues[offset] // Can't assert here, offset might be something absurd.
    }

    /**
     * Reads in a number and spits out a list of values.
     *
     * @param number The number to read. Must not be negative.
     *
     * @return a [List] of [Enum]s of type [T].
     */
    fun read(number: Long): EnumSet<T> {
        val values = EnumSet.noneOf(enumClass)

        for (offset in 0L..offsetsToEnumValues.size) {
            if (number and (1L shl offset.toInt()) != 0L)
                if (offsetsToEnumValues[offset] != null)
                    values.add(offsetsToEnumValues[offset]!!)
        }

        return values
    }

    /**
     * Writes a given [EnumSet] to a [Long] value. Note that for [Enum]s with more than 64 values, you should use
     * [extensiveWrite], as that allows for writing larger sets of enums to a numeric value.
     *
     * @param values The [Enum] values to write to a [Long] format.
     *
     * @return a [Long] encoded from the [Enum] values.
     */
    fun write(values: EnumSet<T>): Long {
        var currentValue = 0L

        for (value in values) {
            val offset = getOffsetFor(value)
            currentValue = currentValue or (1L shl offset.toInt())
        }

        return currentValue
    }

    /**
     * Writes a given [EnumSet] to a [BigInteger] value. Similar to [write], but provides support for an [EnumSet] of more
     * than 64 [Enum] values.
     *
     * @param values The [Enum] values to write to a [BigInteger] format.
     *
     * @return a [BigInteger] encoded from the [Enum] values.
     */
    fun extensiveWrite(values: EnumSet<T>): BigInteger {
        var encodedValue = BigInteger.valueOf(0)

        for (value in values) {
            val offset = getOffsetFor(value)
            encodedValue = encodedValue.setBit(offset.toInt())
        }

        return encodedValue
    }
}
