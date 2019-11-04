package bvanseg.kotlincommons.observable

import bvanseg.kotlincommons.any.toObservable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * @author bright_spark
 */
internal class ObservableTest {
    companion object {
        private const val value1 = "Hello World"
        private const val value2 = "Hello Realm"
    }

    @Test
    fun get() {
        // Given
        val observable = Observable(value1)

        // When
        val value = observable.get()

        // Then
        assertEquals(value1, value)
    }

    @Test
    fun set() {
        // Given
        val observable = Observable(value1)

        // When
        observable.set(value2)

        // Then
        assertEquals(value2, observable.get())
    }

    @Test
    fun getCallback() {
        // Given
        val observable = Observable(value1)
        observable.getCallback = { assertEquals(value1, it) }

        // When
        // Then
        observable.get()
    }

    @Test
    fun setCallback() {
        // Given
        val observable = Observable(value1)
        observable.setCallback = { old, new ->
            assertEquals(value1, old)
            assertEquals(value2, new)
        }

        // When
        // Then
        observable.set(value2)
    }

    @Test
    fun conversion() {
        // Given
        val value = "Hello, world!"
        val obs = value.toObservable()

        // When
        // Then
        assertEquals("Hello, world!", obs.get())
    }
}