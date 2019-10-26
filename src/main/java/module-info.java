/**
 * The module info for KotlinCommons.
 *
 * @author Boston Vanseghi
 */
module kotlincommons {
    requires org.apache.commons.lang3;

    requires static org.junit.jupiter;
    requires static ubjson;
    requires static org.joml;
    requires static com.github.simplenet;
    requires org.slf4j;

    requires kotlin.stdlib;
    requires java.management;

    exports bvanseg.kotlincommons.any;
    exports bvanseg.kotlincommons.assets;
    exports bvanseg.kotlincommons.buffers;
    exports bvanseg.kotlincommons.bytes;
    exports bvanseg.kotlincommons.classes;
    exports bvanseg.kotlincommons.collections;
    exports bvanseg.kotlincommons.comparable;
    exports bvanseg.kotlincommons.compression;
    exports bvanseg.kotlincommons.joml.vectors;
    exports bvanseg.kotlincommons.kclasses;
    exports bvanseg.kotlincommons.logging;
    exports bvanseg.kotlincommons.numbers;
    exports bvanseg.kotlincommons.projects;
    exports bvanseg.kotlincommons.simplenet.packet;
    exports bvanseg.kotlincommons.string;
    exports bvanseg.kotlincommons.system;
    exports bvanseg.kotlincommons.time;
    exports bvanseg.kotlincommons.ubjson;
}