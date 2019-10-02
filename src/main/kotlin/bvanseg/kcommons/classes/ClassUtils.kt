package bvanseg.kcommons.classes

import java.lang.reflect.Constructor

fun <T> createNewInstance(clazz: Class<T>): T? = createNewInstance(clazz, null)

fun <T> createNewInstance(clazz: Class<T>, parameterTypes: Array<Class<*>>?, vararg arguments: Any): T {
    lateinit var constructor: Constructor<T>
    try {
        constructor = if (parameterTypes == null)
            clazz.getDeclaredConstructor()
        else
            clazz.getDeclaredConstructor(*parameterTypes)
    } catch(e: Exception) {
        e.printStackTrace()
    }
    return constructor.newInstance(*arguments)
}