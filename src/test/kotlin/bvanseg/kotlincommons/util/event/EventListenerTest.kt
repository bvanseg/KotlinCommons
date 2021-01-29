package bvanseg.kotlincommons.util.event

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
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

    @Test
    fun testEventChannelFiring() = runBlocking {
        GlobalScope.launch {
            delay(2000L)
            EventBus.DEFAULT.fire("Hello, world!")
        }

        val sub = EventBus.DEFAULT.eventChannel.openSubscription()

        val message = sub.receive()

        assertEquals("Hello, world!", message)

        sub.cancel()
    }
}