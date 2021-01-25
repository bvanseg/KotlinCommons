package bvanseg.kotlincommons.io.logging

import bvanseg.kotlincommons.any.getLogger
import org.junit.jupiter.api.Test

class TestLogging {

    private val logger = getLogger()

    @Test
    fun testLogger() {
        logger.trace("Hello World")
        logger.debug("Hello World")
        logger.info("Hello World")
        logger.warn("Hello World")
        logger.error("Hello World")
    }
}