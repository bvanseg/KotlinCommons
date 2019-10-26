package bvanseg.kcommons.string

import bvanseg.kcommons.comparable.clamp
import java.nio.ByteBuffer

class UTFBuffer private constructor() {

    private var data: StringBuilder = StringBuilder()
    private var originalData = ""

    /** REGEXES **/
    private val numberRegex = Regex("[+-]?([0-9]*[.])?[0-9]+")

    companion object {
        fun wrap(string: String): UTFBuffer {
            val buffer = UTFBuffer()
            buffer.data.clear()
            buffer.originalData = string
            buffer.data.append(string)
            return buffer
        }
    }


    /**
     * Resets the buffer.
     */
    fun reset(string: String): UTFBuffer {
        data.clear()
        data.append(string)
        return this
    }

    /**
     * Gets the next character in the buffer.
     */
    fun nextChar(): Char {
        val char = data[0]
        data.delete(0, 1)
        return char
    }

    /**
     * Returns the next specified amount of characters as an array of characters.
     */
    fun next(amount: Int): CharArray {
        val sample = data.substring(0, amount)
        data.delete(0, amount)
        return sample.toCharArray()
    }

    /**
     * Returns the next word in the buffer.
     */
    fun nextWord(): String = upUntil(' ').trim()

    /**
     * Gets words in-between the specified text.
     */
    fun getBetween(lower: String, upper: String): String {
        val index = data.indexOf(lower, 0) + 1

        val lastIndex = clamp(data.lastIndexOf(upper), 0, data.length - 1)

        val match = data.substring(index, lastIndex)

        return match.trim()
    }

    /**
     * Returns a word prior to the specific text.
     */
    fun getWordBefore(string: String): String {
        val copy = data.toString()
        data.reverse()
        val stringRev = string.reversed()

        val workingPosition = data.indexOf(stringRev)
        data.delete(0, workingPosition + stringRev.length)

        val word = nextWord().reversed()

        this.reset(copy)
        return word
    }

    /**
     * Returns the word after the specified string.
     */
    fun getWordAfter(string: String): String {
        val copy = data.toString()

        val workingPosition = data.indexOf(string)
        data.delete(0, workingPosition + string.length + 1)

        val word = nextWord()

        this.reset(copy)
        return word
    }

    /**
     * Returns the next number in the buffer.
     */
    fun nextNumber(): Number? {
        val d = numberRegex.find(this.data)
        val capture = d?.value?.toDoubleOrNull()
        d?.let {

            data.delete(0, it.range.last + 1)
        }
        return capture
    }

    /**
     * Returns the next line in the buffer.
     */
    fun nextLine(): String = upUntil('\n').trim()

    fun nextUpto(string: String): String = upUntil(string).trim()

    /**
     * Gets string upto the specified number of selectors. Returns a Pair with the text and selector first used to collect the text.
     */
    fun uptoWithSelector(vararg candidates: String): Pair<String, String> {
        var match = ""
        var index = 0
        var selector = ""
        candidates.forEach {
            if (data.contains(it)) {
                index = data.trim().indexOf(it, 0)
                if (index == -1) index = data.length

                match = data.trim().substring(0, index)
                selector = it
                return@forEach
            }
        }

        return Pair(match, selector)
    }

    /**
     * Gets text after the specified String.
     */
    fun after(string: String): String {
        val index = data.trim().indexOf(string, 0) + 1

        val match = data.trim().substring(index, data.length)

        data.delete(0, index)

        return match.trim()
    }

    /**
     * Removes any of the matching text within the data.
     */
    fun strip(string: String): String {
        val newData = data.toString().replace(string, "")
        data.clear()
        data.append(newData)
        return newData
    }

    /** ############################ UTILITY FUNCTIONS ############################ **/

    fun isEmpty(): Boolean = data.isEmpty()

    fun isNotEmpty(): Boolean = data.isNotEmpty()

    private fun toByteBuffer(): ByteBuffer = ByteBuffer.wrap(data.toString().toByteArray())
    private fun toByteArray(): ByteArray = data.toString().toByteArray()


    /** ############################ INTERNAL UTILITY FUNCTIONS ############################ **/

    /**
     * Gets text up until the specified character. The character in question is not consumed.
     */
    private fun upUntil(char: Char): String {
        var index = data.trim().indexOfFirst { it == char }
        if (index == -1) index = data.length
        val match =
            if (index == -1 || data.isEmpty())
                data.trim().toString()
            else
                data.trim().substring(0, clamp(index, 0, data.length))

        data.delete(0, index + 1)

        return match
    }

    /**
     * Gets text up until the specified string. The string in question is not consumed.
     */
    private fun upUntil(string: String): String {
        var index = data.trim().indexOf(string, 0)
        if (index == -1) index = data.length

        val match = data.trim().substring(0, index)

        data.delete(0, index)

        return match
    }

    /** ############################ OVERRIDES ############################ **/

    override fun toString(): String {
        return data.toString()
    }
}