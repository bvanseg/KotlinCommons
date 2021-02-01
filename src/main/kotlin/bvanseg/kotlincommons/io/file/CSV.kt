package bvanseg.kotlincommons.io.file

import bvanseg.kotlincommons.io.logging.getLogger
import bvanseg.kotlincommons.io.logging.warn
import bvanseg.kotlincommons.util.project.Experimental
import java.io.BufferedWriter
import java.nio.file.Files
import java.nio.file.OpenOption
import java.nio.file.Path
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

/**
 * A class that can write to a file in the CSV (comma-separated values) format. Capable of writing objects or just
 * arbitrary rows.
 *
 * When writing objects, the [CSV] instance will index the object classes in order to order them properly, as fetching
 * member properties via Kotlin reflection does not return the properties as they appear (only fetching them from the
 * constructor objects will maintain the order).
 *
 * It is **required** that written objects be Kotlin data classes, as those are guaranteed to have properties to write.
 *
 * @param fileName The name of the file that will be written to.
 *
 * @author Boston Vanseghi
 * @since 2.7.0
 */
class CSV(fileName: String, vararg options: OpenOption) : AutoCloseable {

    companion object {
        private val logger = getLogger()
        private val regex: Regex = Regex("(?=[A-Z])")
    }

    /**
     * NIO Buffered Writer for quick writing.
     */
    private val writer: BufferedWriter = Files.newBufferedWriter(Path.of("$fileName.csv"), *options)

    /**
     * Index object classes to their properties and corresponding getters for quick access.
     */
    private val indices = hashMapOf<KClass<*>, HashMap<String, KProperty1.Getter<out Any, Any?>>>()

    /**
     * Index object classes to their properties and corresponding getters for quick access.
     */
    private val parameterNameCache = hashMapOf<KClass<*>, HashMap<String, String>>()

    /**
     * Append calls to an in-memory builder to help performance.
     */
    private val builder = StringBuilder()

    fun appendHeader(kclass: KClass<*>, flush: Boolean = false): Boolean {
        if (kclass.isData) {
            // If the KClass has not been indexed, do so.
            cacheKClassType(kclass)

            kclass.primaryConstructor?.parameters?.forEach { kParameter ->
                if (parameterNameCache[kclass] == null) {
                    parameterNameCache[kclass] = hashMapOf()
                }

                var formattedName = parameterNameCache[kclass]?.get(kParameter.name)

                if (formattedName == null) {
                    formattedName = kParameter.name?.split(regex)?.joinToString(" ") { it.capitalize() }
                    kParameter.name?.let { name ->
                        parameterNameCache[kclass]!!.put(name, formattedName ?: name)
                    }
                }

                builder.append(formattedName)
                builder.append(',')
            }
            builder.append('\n')
            writer.append(builder.toString())
            builder.setLength(0)

            if (flush) {
                flush()
            }

            return true
        } else {
            logger.warn { "CSV header-writing is for data class types only, class '$kclass' is not a data class!" }
        }

        return false
    }

    fun appendHeader(obj: Any, flush: Boolean = false): Boolean = appendHeader(obj::class, flush)

    fun append(obj: Any, flush: Boolean = false): Boolean {
        val kclass = obj::class

        if (kclass.isData) {
            // If the KClass has not been indexed, do so.
            cacheKClassType(kclass)

            kclass.primaryConstructor?.parameters?.forEach {
                val mappings = indices[kclass]!!
                val getter = mappings[it.name]

                if (getter?.isAccessible == false) {
                    getter.isAccessible = true
                }

                val value = getter?.call(obj)

                if (value != null) {
                    if (value is Array<*>) {
                        builder.append(value.contentToString())
                    } else {
                        builder.append(value.toString())
                    }
                } else {
                    builder.append("NULL")
                }
                builder.append(',')
            }
            builder.append('\n')
            writer.append(builder.toString())
            builder.setLength(0)

            if (flush) {
                flush()
            }

            return true
        } else {
            logger.warn { "CSV appending is for data class types only, class '$kclass' is not a data class!" }
        }

        return false
    }

    fun appendRow(vararg items: Any, flush: Boolean = false) {
        for (item in items) {
            writer.append(item.toString())
            writer.append(",")
        }
        writer.append("\n")

        if (flush) {
            flush()
        }
    }

    override fun close() {
        writer.close()
    }

    /**
     * Flushes the backing [BufferedWriter] of the CSV file.
     */
    fun flush() = writer.flush()

    private fun cacheKClassType(kclass: KClass<*>) {
        if (indices[kclass] == null) {
            val mappings = hashMapOf<String, KProperty1.Getter<out Any, Any?>>()
            indices[kclass] = mappings

            // map property names to getters
            kclass.memberProperties.forEach {
                mappings[it.name] = it.getter
            }
        }
    }
}