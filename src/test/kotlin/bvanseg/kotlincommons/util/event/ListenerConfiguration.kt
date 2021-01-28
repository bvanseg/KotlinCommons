package bvanseg.kotlincommons.util.event

object BasicListener {

    @SubscribeEvent
    fun parentHandler(parent: Parent) = Unit

    @SubscribeEvent
    fun childHandler(child: Child) = Unit
}

open class Parent
class Child : Parent()