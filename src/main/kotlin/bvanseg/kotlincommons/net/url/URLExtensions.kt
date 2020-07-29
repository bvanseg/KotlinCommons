package bvanseg.kotlincommons.net.url

import bvanseg.kotlincommons.string.remove
import java.net.URL

/**
 * Returns the URL without the file:/ protocol, if it has it
 *
 * TODO: Remove arbitrary starting protocol
 *
 * @author CraftSpider
 * @since 2.0.0
 */
fun URL.toPath(): String =
    if (System.getProperty("os.name").contains("Windows")) toString().remove("file:/")
    else toString().remove("file:")