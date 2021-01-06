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
package bvanseg.kotlincommons.event.bus

import bvanseg.kotlincommons.any.getLogger
import bvanseg.kotlincommons.event.annotation.SubscribeEvent
import bvanseg.kotlincommons.event.event.InternalEvent
import bvanseg.kotlincommons.kclass.getKClass
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

    private val listeners: MutableList<Any> = mutableListOf()
    private val listenerEvents: HashMap<Class<*>, MutableList<InternalEvent>> = hashMapOf()
    private val events: HashMap<Class<*>, MutableList<InternalEvent>> = hashMapOf()

    fun addListener(listener: Any) {
        listener::class.memberFunctions.filter { it.findAnnotation<SubscribeEvent>() != null }.forEach { function ->

            if (!function.isAccessible) {
                function.javaMethod?.trySetAccessible()
            }

            val event = InternalEvent(function, listener)
            function.valueParameters.firstOrNull()?.let { param ->
                val clazz = param.type.getKClass().java
                if (events[clazz] == null)
                    events[clazz] = mutableListOf()

                events[clazz]?.let {
                    if (it.add(event))
                        logger.debug("Successfully added event $event with parameter type $clazz for listener $listener")
                } ?: logger.warn("Failed to add event $event for listener $listener!")

            }
                ?: throw RuntimeException("Failed to add event listener. Subscribed event function must have a single parameter!")

            if (listenerEvents[listener::class.java] == null) {
                listenerEvents[listener::class.java] = mutableListOf()
            }

            listenerEvents[listener::class.java]!!.add(event)
        }

        listeners.add(listener)
        logger.debug("Successfully added listener $listener")
    }

    fun removeListener(listener: Any) = listeners.remove(listener)

    fun fire(e: Any) {
        events[e::class.java]?.let { list ->
            list.forEach {
                logger.debug("Firing event $it with object $e")
                it.invoke(e)
            }

            // Walk up the superclasses and fire those, as well.
            for (c in e::class.superclasses) {
                events[c.java]?.let {
                    it.forEach {
                        it.invoke(e)
                    }
                }
            }
        }
    }

    fun fireForListener(listener: Any, event: Any) = fireForListener(listener::class.java, event)

    fun fireForListener(listener: Class<*>, event: Any) {
        listenerEvents[listener]?.forEach {
            it.function.valueParameters.firstOrNull()?.let { param ->
                val clazz = param.type.getKClass().java

                if (event::class.java == clazz) {
                    it.invoke(event)
                }

                // Walk up the superclasses and fire those, as well.
                for (c in event::class.superclasses) {
                    if (clazz == c.java) {
                        it.invoke(event)
                    }
                }
            }
        }
    }
}