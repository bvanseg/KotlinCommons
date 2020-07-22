package bvanseg.kotlincommons.monads

/**
 * ```
 * val myInt = 1.some()
 * val myString = "hello, world".some()
 * val myNewInt = myInt.map{
 *  it + 1
 * }
 * val myNewString = myString.map{
 *  it += "!"
 * }
 * ```
 */

sealed class Option<T>{
    data class Some<T>(val t: T): Option<T>()
    class None<T>: Option<T>(){
        companion object
    }

    val isSome get() = this is Some<T>
    val isNone get() = this is None
    val unwrap get() = if(this is Some<T>) t else throw IllegalStateException("Attempted to unwrap Option but was None")

    fun unwrapOrElse(orElse: ()->T): T =
        if(this is Some<T>) t else orElse()

    fun <S> map(callback: (T)->S): Option<S> =
        if(this is Some<T>){
            callback(t).some()
        }else{
            none()
        }

    @Suppress("UNCHECKED_CAST")
    internal fun <S> fix(): Option<S> = this as Option<S>

    fun <S> flatMap(callback: (T)->S): Option<S> =
        when(this){
            is Some<T> -> {
                when(val result = callback(t)){
                    is Some<*> -> result.t.some().fix()
                    else -> result.some()
                }
            }
            else -> none()
        }
}

fun <T> T.some() = Option.Some(this)
fun <T> none() = Option.None<T>()