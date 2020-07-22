package bvanseg.kotlincommons.monads
//
///**
// * val result = someFunction()
// * return result.flatMap{
// *  "An error occurred while doing something: $it"
// * }
// */
//sealed class Result<out S, out E>{
//    data class Ok<out S>(val value: S): Result<S, Nothing>()
//    class Error<out E>(val value: E): Result<Nothing, E>(){
//        fun <T> flatMap(callback: (E)->T): Error<T>{
//            val result = callback(value)
//            return if(callback is Error<*>){
//                callback.value.error()
//            }else{
//                callback.error()
//            }
//        }
//    }
//
//    val isOk get() = this is Ok<S>
//    val isError get() = this is Error<E>
//    val unwrap get() = if(this is Ok<S>) value else throw IllegalStateException("Attempted to unwrap Option but was Error: ${(this as Error).value}")
//
//    fun unwrapOrElse(orElse: ()->S): S =
//        if(this is Ok<S>) value else orElse()
//
//    @Suppress("UNCHECKED_CAST")
//    internal fun <S> fixError(): Error<S> = this as Error<S>
//
//    @Suppress("UNCHECKED_CAST")
//    internal fun <S> fixOk(): Ok<S> = this as Ok<S>
//
////    fun <T, U> flatMap(callback: (T) -> U) =
////        when(this){
////            is Ok<*> -> flatMap(callback)
////            is Err<*> -> flatMap(callback)
////        }
//}
//
//fun <T> T.ok() = Result.Ok(this)
//fun <T> T.error() = Result.Error(this)