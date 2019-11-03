package bvanseg.kotlincommons.observable

class Observable<T>(private var value: T? = null) {
    fun set(newValue: T?) {
        setCallback(value, newValue)
        value = newValue
    }

    fun get(): T? {
        getCallback(value)
        return value
    }

    var setCallback: (old: T?, new: T?) -> Unit = { _, _ -> }
    var getCallback: (T?) -> Unit = {}
}