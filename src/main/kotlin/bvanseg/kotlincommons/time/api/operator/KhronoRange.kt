package bvanseg.kotlincommons.time.api.operator

import bvanseg.kotlincommons.time.api.Khrono
import bvanseg.kotlincommons.time.api.MutableKhrono

/*
    IMMUTABLE
 */
operator fun Khrono.rangeTo(other: Khrono): Khrono = other - this

/*
    MUTABLE
 */
operator fun MutableKhrono.rangeTo(other: MutableKhrono): Khrono = other - this