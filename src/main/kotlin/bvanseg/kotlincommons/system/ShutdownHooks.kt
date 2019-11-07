package bvanseg.kotlincommons.system

private val hooks = mutableSetOf<() -> Unit>()

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