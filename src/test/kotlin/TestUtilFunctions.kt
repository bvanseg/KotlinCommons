import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream

/**
 * @author bright_spark
 */

/**
 * Creates a [Stream] of [Arguments] created from the given [args]
 */
fun argStreamOf(vararg args: Any?): Stream<Arguments> = args.map { Arguments.of(it) }.stream()