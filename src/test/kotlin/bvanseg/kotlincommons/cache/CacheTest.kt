package bvanseg.kotlincommons.cache

import bvanseg.kotlincommons.project.Experimental
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@Experimental
class CacheTest {

    @Test
    fun testCacheCreation() {
        // GIVEN
        val cache = Cache<Long, String>(60_000L)

        // WHEN
        cache.give(0, "Bob")

        // THEN
        // Test that value is actually stored.
        assertEquals("Bob", cache.get(0))
        // Test that the next value is some null value.
        assertEquals(null, cache.get(1))
    }

    @Test
    fun testCacheClear() {
        // GIVEN
        val cache = Cache<Long, String>(60_000L)
        cache.give(0, "Bob")

        // WHEN
        cache.clear()

        // THEN
        assertTrue(cache.isEmpty())
    }

    @Test
    fun testCacheFetchInvalidation() {
        // GIVEN
        val cache = Cache<Long, String>(60_000L)

        // WHEN
        cache.give(0, "Bob")

        // THEN
        assertEquals("Bob", cache.getAndInvalidate(0))
        assertTrue(cache.isEmpty())
    }
}