package bvanseg.kotlincommons.io.file

import bvanseg.kotlincommons.util.project.Experimental
import java.nio.file.Files
import java.nio.file.Path
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

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
@Experimental
class CSV(fileName: String): AutoCloseable {

    /**
     * NIO Buffered Writer for quick writing.
     */
    private val writer = Files.newBufferedWriter(Path.of("$fileName.csv"))

    /**
     * Index object classes to their properties and corresponding getters for quick access.
     */
    private val indices = hashMapOf<KClass<*>, HashMap<String, KProperty1.Getter<out Any, Any?>>>()

    /**
     * Append calls to an in-memory builder to help performance.
     */
    private val builder = StringBuilder()

    fun append(obj: Any): Boolean {
        val kclass = obj::class

        if (kclass.isData) {
            // If the KClass has not been indexed, do so.
            if (indices[kclass] == null) {
                val mappings = hashMapOf<String, KProperty1.Getter<out Any, Any?>>()
                indices[kclass] = mappings

                // map property names to getters
                kclass.memberProperties.forEach {
                    mappings[it.name] = it.getter
                }
            }

            kclass.primaryConstructor?.parameters?.forEach {
                val mappings = indices[kclass]!!
                val getter = mappings[it.name]

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
            builder.clear()
            return true
        }

        return false
    }

    fun appendRow(vararg items: Any) {
        for (item in items) {
            writer.append(item.toString())
            writer.append(",")
        }
        writer.append("\n")
    }

    override fun close() {
        writer.close()
    }
}