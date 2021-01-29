package bvanseg.kotlincommons.lang.thread

import bvanseg.kotlincommons.KotlinCommons
import java.util.concurrent.ConcurrentHashMap

private fun validateThreadAttributeMap() {
    val currentThreadID = Thread.currentThread().id

    if (KotlinCommons.KC_THREAD_ATTRIBUTE_MAP[currentThreadID] == null) {
        KotlinCommons.KC_THREAD_ATTRIBUTE_MAP[currentThreadID] = ConcurrentHashMap()
    }
}

fun <T> Thread.getAttribute(name: String): T? {
    validateThreadAttributeMap()
    val currentThreadID = this.id
    @Suppress("UNCHECKED_CAST")
    val value = KotlinCommons.KC_THREAD_ATTRIBUTE_MAP[currentThreadID]!![name]?.get() as T?
    return value
}

fun Thread.removeAttribute(name: String) = KotlinCommons.KC_THREAD_ATTRIBUTE_MAP[this.id]?.let {
    it[name]?.remove()
    it.remove(name)
}

fun <T> Thread.setAttribute(name: String, value: T): T {
    validateThreadAttributeMap()
    val currentThreadID = this.id

    if (KotlinCommons.KC_THREAD_ATTRIBUTE_MAP[currentThreadID]!![name] == null) {
        KotlinCommons.KC_THREAD_ATTRIBUTE_MAP[currentThreadID]!![name] = ThreadLocal<T>()
    }

    @Suppress("UNCHECKED_CAST") val attribute = KotlinCommons.KC_THREAD_ATTRIBUTE_MAP[currentThreadID]!![name]!! as ThreadLocal<T>

    attribute.set(value)

    return value
}

fun Thread.clearAttributes() = KotlinCommons.KC_THREAD_ATTRIBUTE_MAP.remove(this.id)