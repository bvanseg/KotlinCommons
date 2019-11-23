/*
 * MIT License
 *
 * Copyright (c) 2019 Boston Vanseghi
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
package bvanseg.kotlincommons.evenir.bus

import bvanseg.kotlincommons.any.getLogger
import bvanseg.kotlincommons.evenir.annotations.SubscribeEvent
import bvanseg.kotlincommons.evenir.event.InternalEvent
import bvanseg.kotlincommons.kclasses.getKClass
import java.lang.RuntimeException
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.valueParameters

/**
 * The primary manager for events. Registers listeners and fires events.
 *
 * @author Boston Vanseghi
 * @since 2.1.0
 */
class EventBus {

    private val listeners: MutableList<Any> = mutableListOf()

    private val events: HashMap<Class<*>, MutableList<InternalEvent>> = hashMapOf()

    fun addListener(listener: Any) {
        listener::class.memberFunctions.filter { it.findAnnotation<SubscribeEvent>() != null }.forEach {
            val event = InternalEvent(it, listener)
            it.valueParameters.firstOrNull()?.let {
                val clazz = it.type.getKClass().java
                if(events[clazz] == null)
                    events[clazz] = mutableListOf()

                events[clazz]?.let {
                    if(it.add(event))
                        getLogger().debug("Successfully added event $event with parameter type $clazz for listener $listener")
                } ?: getLogger().warn("Failed to add event $event for listener $listener!")

            } ?: throw RuntimeException("Failed to add event listener. Subscribed event function must have a single parameter!")
        }

        listeners.add(listener)
        getLogger().debug("Successfully added listener $listener")
    }

    fun removeListener(listener: Any) = listeners.remove(listener)

    fun fire(e: Any) {
        getLogger().debug("Preparing to fire all events with target ${e::class.java}...")
        events[e::class.java]?.let {
            it.forEach {
                getLogger().debug("Firing event $it with object $e")
                it.invoke(e)
            }
        } ?: getLogger().warn("Failed to fire event for event $e with type ${e::class.java}!")
    }

    companion object {
        @JvmStatic
        val DEFAULT = EventBus()
    }
}