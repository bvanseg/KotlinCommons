package bvanseg.kotlincommons.util.event

import bvanseg.kotlincommons.util.event.bus.EventBus
import org.junit.jupiter.api.Test

class EventHierarchyTest {

    @Test
    fun testHierarchy() {
        EventBus.DEFAULT.addListener(BasicListener)
        EventBus.DEFAULT.fire(Child())
    }
}