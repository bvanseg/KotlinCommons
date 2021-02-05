/*
 * MIT License
 *
 * Copyright (c) 2021 Boston Vanseghi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package bvanseg.kotlincommons.util.event

import bvanseg.kotlincommons.io.logging.getLogger
import bvanseg.kotlincommons.reflect.getKClass
import bvanseg.kotlincommons.time.api.Khrono
import bvanseg.kotlincommons.util.any.delay
import bvanseg.kotlincommons.util.concurrent.KCountDownLatch
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.superclasses
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaMethod

/**
 * The primary manager for events. Registers listeners and fires events.
 *
 * @author Boston Vanseghi
 * @since 2.1.0
 */
@Suppress("UNCHECKED_CAST")
class EventBus {

    companion object {
        @JvmStatic
        val DEFAULT = EventBus()

        private val logger = getLogger()
    }

    /**
     * Used to prevent the [EventBus] from firing events.
     */
    var isEnabled = true

    /**
     * Used to lock the [EventBus] and prevent further event listener additions/removals.
     *
     * WARNING: Once the event bus is locked, it can not be unlocked through normal access.
     */
    var isLocked = false
        private set

    fun lock() {
        isLocked = true
    }

    /**
     * A collection of listener objects.
     */
    private val listeners: MutableList<Any> = mutableListOf()

    /**
     * A collection that maps a listener class to its [InternalEvent] handlers.
     */
    private val listenerEvents: ConcurrentHashMap<Class<*>, MutableList<InternalEvent>> = ConcurrentHashMap()

    /**
     * A collection that maps an event type to its [InternalEvent] handlers.
     */
    val persistentCallbackListeners: ConcurrentHashMap<Class<*>, MutableList<CallbackEvent<*>>> = ConcurrentHashMap()

    /**
     * A collection that maps an event type to its [InternalEvent] handlers.
     */
    val callbackListeners: ConcurrentHashMap<Class<*>, MutableList<CallbackEvent<*>>> = ConcurrentHashMap()

    /**
     * A collection that maps an event type to its [InternalEvent] handlers.
     */
    private val events: ConcurrentHashMap<Class<*>, MutableList<InternalEvent>> = ConcurrentHashMap()

    fun addListener(listener: Any) {
        if (isLocked) return

        listener::class.memberFunctions.filter { it.findAnnotation<SubscribeEvent>() != null }.forEach { function ->

            if (!function.isAccessible) {
                function.javaMethod?.trySetAccessible()
            }

            val event = InternalEvent(function, listener)
            function.valueParameters.firstOrNull()?.let { parameter ->
                val clazz = parameter.type.getKClass().java
                if (events[clazz] == null)
                    events[clazz] = mutableListOf()

                events[clazz]?.let {
                    if (it.add(event))
                        logger.debug(
                            "Successfully added event {} with parameter type {} for listener {}",
                            event,
                            clazz,
                            listener
                        )
                } ?: logger.warn("Failed to add event {} for listener {}!", event, listener)

            }
                ?: throw RuntimeException("Failed to add event listener. Subscribed event function must have a single parameter!")

            if (listenerEvents[listener::class.java] == null) {
                listenerEvents[listener::class.java] = mutableListOf()
            }

            listenerEvents[listener::class.java]!!.add(event)
        }

        synchronized(listeners) {
            listeners.add(listener)
        }

        logger.debug("Successfully added listener {}", listener)
    }

    /**
     * Registers a callback listener.
     *
     * @param persist Whether or not the given [callback] should persist as an event handler.
     * @param callback The callback to execute upon receiving the target event.
     */
    inline fun <reified T : Any> on(persist: Boolean = false, noinline callback: (T) -> Unit) {
        if (isLocked) return

        val callbackClass = T::class.java

        if (persist) {
            persistentCallbackListeners.computeIfAbsent(callbackClass) { mutableListOf() }.add(CallbackEvent(callback))
        } else {
            callbackListeners.computeIfAbsent(callbackClass) { mutableListOf() }.add(CallbackEvent(callback))
        }
    }

    /**
     * Removes the given [listener] object.
     */
    fun removeListener(listener: Any){
        if (isLocked) return

        synchronized(listeners) {
            listeners.remove(listener)
            listenerEvents.remove(listener::class.java)
        }
    }

    /**
     * Gets the class of the given event and fires all corresponding handlers for it.
     *
     * @param event The event to fire.
     */
    fun fire(event: Any) {
        if (!isEnabled) return

        val eventClass = event::class.java

        events[event::class.java]?.let { internalEvents ->
            internalEvents.forEach { internalEvent ->
                logger.debug("Firing event {} with object {}", internalEvent, event)
                try {
                    internalEvent.invoke(event)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            // Walk up the superclasses and fire those, as well.
            for (superClass in event::class.superclasses) {
                events[superClass.java]?.let { superClassInternalEvents ->
                    superClassInternalEvents.forEach { internalEvent ->
                        try {
                            internalEvent.invoke(event)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }

        callbackListeners.computeIfPresent(eventClass) { _, value ->
            value.forEach { callbackEvent ->
                (callbackEvent as CallbackEvent<Any>).invoke(event)

                // Walk up the superclasses and fire those, as well.
                for (superClass in event::class.superclasses) {
                    callbackListeners.computeIfPresent(superClass.java) { _, v ->
                        v.forEach { superCallbackEvent ->
                            try {
                                (superCallbackEvent as CallbackEvent<Any>).invoke(event)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        null
                    }
                }
            }
            null
        }

        persistentCallbackListeners.computeIfPresent(eventClass) { _, value ->
            value.forEach { callbackEvent ->
                try {
                    (callbackEvent as CallbackEvent<Any>).invoke(event)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                // Walk up the superclasses and fire those, as well.
                for (superClass in event::class.superclasses) {
                    persistentCallbackListeners.computeIfPresent(superClass.java) { _, v ->
                        v.forEach { superCallbackEvent ->
                            try {
                                (superCallbackEvent as CallbackEvent<Any>).invoke(event)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        null
                    }
                }
            }
            null
        }
    }

    fun fireForListener(listener: Any, event: Any) = fireForListener(listener::class.java, event)

    fun fireForListener(listener: Class<*>, event: Any) {
        if (!isEnabled) return

        listenerEvents[listener]?.forEach { internalEvent ->
            internalEvent.function.valueParameters.firstOrNull()?.let { param ->
                val parameterClass = param.type.getKClass().java

                if (event::class.java == parameterClass) {
                    try {
                        internalEvent.invoke(event)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                // Walk up the superclasses and fire those, as well.
                for (superClass in event::class.superclasses) {
                    if (parameterClass == superClass.java) {
                        try {
                            internalEvent.invoke(event)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    /**
     * Schedules an event to fire after the given [Khrono] time.
     *
     * @param event The event to fire.
     * @param delay The [Khrono] delay before the event is fired.
     *
     * @return A [kotlinx.coroutines.Job] of the scheduled event fire, which can be cancelled.
     */
    fun scheduleFire(event: Any, delay: Khrono) = GlobalScope.launch {
        if (!isEnabled) return@launch
        delay(delay)
        this@EventBus.fire(event)
    }

    /**
     * Awaits an event, blocking the current thread until the event is fired by this [EventBus].
     *
     * @param T The type of event to await. Superclasses of specific events are also acceptable to await.
     */
    inline fun <reified T : Any> awaitThreadEvent() {
        if (!isEnabled) return
        val latch = CountDownLatch(1)

        this.on<T> {
            latch.countDown()
        }

        latch.await()
    }

    /**
     * Awaits an event, blocking the current coroutine until the event is fired by this [EventBus].
     * Functions similarly to [awaitThreadEvent], but instead uses a [KCountDownLatch] as opposed to a [CountDownLatch].
     *
     * @param T The type of event to await. Superclasses of specific events are also acceptable to await.
     */
    suspend inline fun <reified T : Any> awaitCoroutineEvent() {
        if (!isEnabled) return
        val latch = KCountDownLatch(1)

        this.on<T> {
            latch.countDown()
        }

        latch.await()
    }

    /**
     * Removes all listeners and clears all internal events.
     */
    fun reset() {
        if (isLocked) return

        synchronized(listeners) {
            listeners.clear()
        }
        listenerEvents.clear()
        persistentCallbackListeners.clear()
        callbackListeners.clear()
        events.clear()
    }
}