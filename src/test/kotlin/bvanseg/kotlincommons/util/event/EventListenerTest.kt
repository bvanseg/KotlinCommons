package bvanseg.kotlincommons.util.event

import bvanseg.kotlincommons.util.event.bus.EventBus
import org.junit.jupiter.api.Test

class EventListenerTest {

    @Test
    fun testListenerSpecificFiring() {
        EventBus.DEFAULT.addListener(BasicListener)
        EventBus.DEFAULT.fireForListener(BasicListener, Child())
        EventBus.DEFAULT.fireForListener(BasicListener::class.java, Child())
    }
}