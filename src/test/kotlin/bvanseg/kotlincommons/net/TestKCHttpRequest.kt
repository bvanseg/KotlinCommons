package bvanseg.kotlincommons.net

import bvanseg.kotlincommons.net.http.httpRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestKCHttpRequest {

    @Test
    fun testHeaderParameterBlocks() {
        // Given
        val r = httpRequest {
            get {
                target = "http://www.google.com/search"

                headers {
                    addHeader("foo", "bar")
                }

                parameters {
                    addParameter("q", "chocolate")
                }
            }
        }

        // When
        // Then
        assertEquals("bar", r.headers().map()["foo"]?.first())
        assertEquals("http://www.google.com/search?q=chocolate", r.uri().toString())
    }
}