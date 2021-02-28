package bvanseg.kotlincommons.lang.checks

import bvanseg.kotlincommons.math.isNegative
import bvanseg.kotlincommons.math.isPositive
import kotlin.jvm.Throws
import kotlin.reflect.KClass

/**
 * Utility object that performs short common checks.
 *
 * @author Boston Vanseghi
 * @since 2.9.7
 */
object Checks {

    @Throws(CheckException::class)
    fun isCompanion(obj: Any?, paramName: String? = null): Unit = if (obj == null || !obj::class.isCompanion) {
        throw CheckException("${paramName ?: "Value"} is not a companion object.")
    } else Unit

    @Throws(CheckException::class)
    fun isDataClass(obj: KClass<*>?, paramName: String? = null): Unit = if (obj == null || !obj.isData) {
        throw CheckException("${paramName ?: "Value"} is not a data class.")
    } else Unit

    @Throws(CheckException::class)
    fun isInstanceOfDataClass(obj: Any?, paramName: String? = null): Unit = if (obj == null || !obj::class.isData) {
        throw CheckException("${paramName ?: "Value"} is not an instance of a data class.")
    } else Unit

    @Throws(CheckException::class)
    fun isNull(obj: Any?, paramName: String? = null): Unit = if (obj != null) {
        throw CheckException("${paramName ?: "Value"} was not null.")
    } else Unit

    @Throws(CheckException::class)
    fun isNotNull(obj: Any?, paramName: String? = null): Unit = if (obj == null) {
        throw CheckException("${paramName ?: "Value"} was null.")
    } else Unit

    // Booleans

    @Throws(CheckException::class)
    fun isTrue(condition: Boolean): Unit = if (!condition) { throw CheckException("Condition was false.") } else Unit

    @Throws(CheckException::class)
    fun isFalse(condition: Boolean): Unit = if (condition) { throw CheckException("Condition was true.") } else Unit

    // Numbers

    @Throws(CheckException::class)
    fun <T: Number> isPositive(value: T, paramName: String? = null): Unit = if (!value.isPositive()) {
        throw CheckException("${paramName ?: "Value"} was not positive: $value")
    } else Unit

    @Throws(CheckException::class)
    fun <T: Number> isNegative(value: T, paramName: String? = null): Unit = if (!value.isNegative()) {
        throw CheckException("${paramName ?: "Value"} was not negative: $value")
    } else Unit

    @Throws(CheckException::class)
    fun <T: Number> isWholeNumber(value: T, paramName: String? = null): Unit = if (value.toDouble() < 0) {
        throw CheckException("${paramName ?: "Value"} was not a whole number: $value")
    } else Unit

    @Throws(CheckException::class)
    fun isFinite(value: Double, paramName: String? = null): Unit = if (!value.isFinite()) {
        throw CheckException("${paramName ?: "Value"} was not finite: $value")
    } else Unit

    @Throws(CheckException::class)
    fun isInfinite(value: Double, paramName: String? = null): Unit = if (!value.isInfinite()) {
        throw CheckException("${paramName ?: "Value"} was not infinite: $value")
    } else Unit

    @Throws(CheckException::class)
    fun isNaN(value: Double, paramName: String? = null): Unit = if (!value.isNaN()) {
        throw CheckException("${paramName ?: "Value"} was a number: $value")
    } else Unit
}