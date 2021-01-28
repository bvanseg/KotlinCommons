package bvanseg.kotlincommons.util.event

import org.junit.jupiter.api.Test

class EventHierarchyTest {

    @Test
    fun testHierarchy() {
        EventBus.DEFAULT.addListener(BasicListener)
        EventBus.DEFAULT.fire(Child())
    }
}