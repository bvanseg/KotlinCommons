package bvanseg.kotlincommons.lang.check

import bvanseg.kotlincommons.math.isEven
import bvanseg.kotlincommons.math.isNegative
import bvanseg.kotlincommons.math.isOdd
import bvanseg.kotlincommons.math.isPositive
import kotlin.reflect.KClass

/**
 * Utility object that performs short common checks.
 *
 * @author Boston Vanseghi
 * @since 2.9.7
 */
object Checks {

    val isCompanion by lazy { Check.create<Any> { it::class.isCompanion } }
    val isDataClass by lazy { Check.create<KClass<*>> { it.isData } }

    val isInstanceOfDataClass by lazy { Check.create<Any> { it::class.isData } }

    val isNull by lazy { Check.create<Any?> { it == null } }
    val isNotNull by lazy { Check.create<Any?> { it != null } }

    val isTrue by lazy { Check.create<Boolean> { it } }
    val isFalse by lazy { Check.create<Boolean> { !it } }

    val isPositive by lazy { Check.create<Number> { it.isPositive() } }
    val isNegative by lazy { Check.create<Number> { it.isNegative() } }
    val isEven by lazy { Check.create<Number> { it.isEven() } }
    val isOdd by lazy { Check.create<Number> { it.isOdd() } }
    val isWholeNumber by lazy { Check.create<Number> { it.toDouble() >= 0 } }
    val isFinite by lazy { Check.create<Double> { it.isFinite() } }
    val isInfinite by lazy { Check.create<Double> { it.isInfinite() } }
    val isNaN by lazy { Check.create<Double> { it.isNaN() } }
}