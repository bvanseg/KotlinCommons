package bvanseg.kotlincommons.event

import bvanseg.kotlincommons.event.bus.EventBus
import org.junit.jupiter.api.Test

class EventListenerTest {

    @Test
    fun testListenerSpecificFiring() {
        EventBus.DEFAULT.addListener(BasicListener)
        EventBus.DEFAULT.fireForListener(BasicListener, Child())
        EventBus.DEFAULT.fireForListener(BasicListener::class.java, Child())
    }
}