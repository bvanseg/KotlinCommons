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

    @Test
    fun testCallbackListeners() {
        var flag1 = false
        var flag2 = false

        EventBus.DEFAULT.on<Child> {
            flag1 = true
        }

        EventBus.DEFAULT.on<Parent> {
            flag2 = true
        }

        EventBus.DEFAULT.fire(Child())

        if (!flag1 || !flag2) {
            assert(false)
        }
    }
}