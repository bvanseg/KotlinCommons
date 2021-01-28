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
package bvanseg.kotlincommons.util.event.bus

import bvanseg.kotlincommons.io.logging.getLogger
import bvanseg.kotlincommons.util.event.annotation.SubscribeEvent
import bvanseg.kotlincommons.util.event.event.InternalEvent
import bvanseg.kotlincommons.reflect.getKClass
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
class EventBus {

    companion object {
        @JvmStatic
        val DEFAULT = EventBus()

        private val logger = getLogger()
    }

    var isEnabled = true

    /**
     * A collection of listener objects.
     */
    private val listeners: MutableList<Any> = mutableListOf()

    /**
     * A collection that maps a listener class to its [InternalEvent] handlers.
     */
    private val listenerEvents: HashMap<Class<*>, MutableList<InternalEvent>> = hashMapOf()

    /**
     * A collection that maps an event type to its [InternalEvent] handlers.
     */
    private val events: HashMap<Class<*>, MutableList<InternalEvent>> = hashMapOf()

    fun addListener(listener: Any) {
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

            } ?: throw RuntimeException("Failed to add event listener. Subscribed event function must have a single parameter!")

            if (listenerEvents[listener::class.java] == null) {
                listenerEvents[listener::class.java] = mutableListOf()
            }

            listenerEvents[listener::class.java]!!.add(event)
        }

        listeners.add(listener)
        logger.debug("Successfully added listener {}", listener)
    }

    /**
     * Removes the given [listener] object.
     */
    fun removeListener(listener: Any) = listeners.remove(listener)

    /**
     * Gets the class of the given event and fires all corresponding handlers for it.
     *
     * @param event The event to fire.
     */
    fun fire(event: Any) {
        if (!isEnabled) return

        events[event::class.java]?.let { internalEvents ->
            internalEvents.forEach { internalEvent ->
                logger.debug("Firing event {} with object {}", internalEvent, event)
                internalEvent.invoke(event)
            }

            // Walk up the superclasses and fire those, as well.
            for (superClass in event::class.superclasses) {
                events[superClass.java]?.let { superClassInternalEvents ->
                    superClassInternalEvents.forEach { internalEvent ->
                        internalEvent.invoke(event)
                    }
                }
            }
        }
    }

    fun fireForListener(listener: Any, event: Any) = fireForListener(listener::class.java, event)

    fun fireForListener(listener: Class<*>, event: Any) {
        if (!isEnabled) return

        listenerEvents[listener]?.forEach { internalEvent ->
            internalEvent.function.valueParameters.firstOrNull()?.let { param ->
                val parameterClass = param.type.getKClass().java

                if (event::class.java == parameterClass) {
                    internalEvent.invoke(event)
                }

                // Walk up the superclasses and fire those, as well.
                for (superClass in event::class.superclasses) {
                    if (parameterClass == superClass.java) {
                        internalEvent.invoke(event)
                    }
                }
            }
        }
    }

    /**
     * Removes all listeners and clears all internal events.
     */
    fun reset() {
        listeners.clear()
        listenerEvents.clear()
        events.clear()
    }
}