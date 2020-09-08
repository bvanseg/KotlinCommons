package bvanseg.kotlincommons.evenir

import bvanseg.kotlincommons.evenir.annotations.SubscribeEvent
import bvanseg.kotlincommons.evenir.bus.EventBus

object BasicListener {

    @SubscribeEvent
    fun parentHandler(parent: Parent) = Unit

    @SubscribeEvent
    fun childHandler(child: Child)  = Unit
}

open class Parent
class Child: Parent()