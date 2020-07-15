package bvanseg.kotlincommons.throwable

/**
 * Takes a [Throwable] and creates a simple analysis of the exception.
 *
 * @author bright_spark
 * @since 2.2.5
 */
class ExceptionAnalysis(val exception: Throwable, val rootPackage: String = defaultRootPackage) {
    companion object {
        /** The default root package, created from the first 3 packages of this class' qualified name */
        var defaultRootPackage = this::class.qualifiedName!!.split('.').take(3).joinToString(".")
    }

    /** The exception message. */
    val message = exception.toString()
    /** A list of each exception that causes the [exception] */
    val cause: List<String>
    /** The root exception that causes the [exception] */
    val rootCause: Throwable

    init {
        cause = mutableListOf<String>()
            .apply {
                var e = exception
                while (e.cause != null) {
                    e = e.cause!!
                    add(e.toString())
                }
                rootCause = e
            }
            .toList()
    }

    /**
     * The line that potentially caused the [exception].
     * This is discovered by analysing package names that match the [rootPackage].
     * */
    val problematicLine =
        rootCause.stackTrace
            .firstOrNull { it.className.startsWith(rootPackage) && it.lineNumber > 0 }
            ?.toString()
            ?: exception.stackTrace[0].toString()

    /**
     * Returns the [exception] as a String.
     * @see [Throwable.printToString]
     */
    fun exceptionToString(): String = exception.printToString()
}
