package bvanseg.kotlincommons.evenir

import bvanseg.kotlincommons.evenir.annotations.SubscribeEvent
import bvanseg.kotlincommons.evenir.bus.EventBus

object BasicListener {

    @SubscribeEvent
    fun parentHandler(parent: Parent)
    {
        println("Hello, world! I am the parent!")
    }

    @SubscribeEvent
    fun childHandler(child: Child)
    {
        println("Hello, world! I am the child!")
    }
}

open class Parent
class Child: Parent()