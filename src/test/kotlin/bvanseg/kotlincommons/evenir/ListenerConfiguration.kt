package bvanseg.kotlincommons.evenir

import bvanseg.kotlincommons.evenir.annotation.SubscribeEvent

object BasicListener {

    @SubscribeEvent
    fun parentHandler(parent: Parent) = Unit

    @SubscribeEvent
    fun childHandler(child: Child) = Unit
}

open class Parent
class Child : Parent()