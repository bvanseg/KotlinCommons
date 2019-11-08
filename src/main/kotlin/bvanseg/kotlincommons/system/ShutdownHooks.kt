package bvanseg.kotlincommons.system

private val hooks = mutableSetOf<() -> Unit>()

/**
 * Schedules a task to be ran when the JVM exits.
 *
 * @param hook - The code to execute on JVM shutdown.
 *
 * @author Boston Vanseghi
 * @since 2.0.2
 */
fun runOnExit(hook: () -> Unit) = ShutdownHooks.run {
    hooks.add(hook)
}

private object ShutdownHooks {
    init {
        Runtime.getRuntime().addShutdownHook(object: Thread() {
            override fun run() {
                hooks.forEach {
                    it.invoke()
                }
            }
        })
    }
}