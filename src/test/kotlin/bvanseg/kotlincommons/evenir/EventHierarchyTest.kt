package bvanseg.kotlincommons.evenir

import bvanseg.kotlincommons.evenir.bus.EventBus
import org.junit.jupiter.api.Test

class EventHierarchyTest {

    @Test
    fun testHierarchy() {
        EventBus.DEFAULT.addListener(BasicListener)
        EventBus.DEFAULT.fire(Child())
    }
}