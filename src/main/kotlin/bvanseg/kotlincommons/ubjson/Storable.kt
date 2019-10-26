package bvanseg.kotlincommons.ubjson

interface Storable {

    /**
     * Serialises any data that needs persisting into a [UBJ] object
     */
    fun writeToUBJ(ubj: UBJ): UBJ

    /**
     * Deserialises the data within a [UBJ] object
     */
    fun readFromUBJ(ubj: UBJ)
}
