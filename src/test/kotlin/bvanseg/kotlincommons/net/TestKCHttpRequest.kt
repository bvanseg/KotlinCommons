package bvanseg.kotlincommons.net

import bvanseg.kotlincommons.net.http.httpRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestKCHttpRequest {

    @Test
    fun testHeaderParameterBlock() {
        // Given
        val r = httpRequest {
            get {
                target = "http://www.google.com/search"

                headers {
                    addHeader("foo", "bar")
                }

                parameters {
                    "q" to "chocolate"
                }
            }
        }

        // When
        // Then
        assertEquals("bar", r.headers().map()["foo"]?.first())
        assertEquals("http://www.google.com/search?q=chocolate", r.uri().toString())
    }

    @Test
    fun testHeaderParameterBlockMapVersion() {
        // Given
        val r = httpRequest {
            get {
                target = "http://www.google.com/search"

                headers {
                    addHeader("foo", "bar")
                }

                parameters(mapOf(
                    "q" to "chocolate"
                ))
            }
        }

        // When
        // Then
        assertEquals("bar", r.headers().map()["foo"]?.first())
        assertEquals("http://www.google.com/search?q=chocolate", r.uri().toString())
    }
}