package bvanseg.kotlincommons.lang.check

import kotlin.jvm.Throws

/**
 * @author Boston Vanseghi
 * @since 2.9.7
 */
class Check<in T> private constructor(private val checkMessage: String, private val checkCallback: (T) -> Boolean) {

    companion object {
        fun <T> create(checkMessage: String = "Check failed for %s: '%s'", checkCallback: (T) -> Boolean): Check<T> = Check(checkMessage, checkCallback)

        fun <T> all(value: T, valueName: String, vararg checks: Check<T>) {
            checks.forEach { it.check(value, valueName) }
        }
    }

    @Throws(CheckException::class)
    fun check(value: T, valueName: String = "value") {
        if (!checkCallback(value)) throw CheckException(checkMessage.format(valueName, value))
    }

    @Throws(CheckException::class)
    fun checkOpposite(value: T, valueName: String = "value") {
        if (checkCallback(value)) throw CheckException(checkMessage.format(valueName, value))
    }
}