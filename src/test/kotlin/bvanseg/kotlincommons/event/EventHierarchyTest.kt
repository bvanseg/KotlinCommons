package bvanseg.kotlincommons.event

import bvanseg.kotlincommons.event.bus.EventBus
import org.junit.jupiter.api.Test

class EventHierarchyTest {

    @Test
    fun testHierarchy() {
        EventBus.DEFAULT.addListener(BasicListener)
        EventBus.DEFAULT.fire(Child())
    }
}