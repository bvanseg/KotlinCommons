package bvanseg.kcommons.ubjson

import com.devsmart.ubjson.*
import java.util.*

/**
 * A wrapper class for [UBObject] with helper functions for getting and setting data
 * @author bright_spark
 */
class UBJ {

    /**
     * The wrapped [UBObject]
     */
    var wrappedUBObject: UBObject
        private set

    /**
     * Creates a new empty [UBObject]
     */
    constructor() {
        wrappedUBObject = UBValueFactory.createObject()
    }

    /**
     * Wraps the given [UBObject]
     */
    constructor(ubObject: UBObject) {
        this.wrappedUBObject = ubObject
    }

    /**
     * Sets a [UBValue] to [UBJ.wrappedUBObject]
     * Used internally in this class only
     */
    private fun set(key: String, value: UBValue) {
        wrappedUBObject[key] = value
    }

    /**
     * Sets a boolean to the wrapped [UBObject]
     */
    fun setBoolean(key: String, value: Boolean) = set(key, UBValueFactory.createBool(value))

    /**
     * Sets a byte to the wrapped [UBObject]
     */
    fun setByte(key: String, value: Byte) = setLong(key, value.toLong())

    /**
     * Sets a byte to the wrapped [UBObject]
     */
    fun setBytes(key: String, value: ByteArray) = set(key, UBValueFactory.createArray(value))

    /**
     * Sets a short to the wrapped [UBObject]
     */
    fun setShort(key: String, value: Short) = setLong(key, value.toLong())

    /**
     * Sets a integer to the wrapped [UBObject]
     */
    fun setInt(key: String, value: Int) = setLong(key, value.toLong())

    /**
     * Sets a long to the wrapped [UBObject]
     */
    fun setLong(key: String, value: Long) = set(key, UBValueFactory.createInt(value))

    /**
     * Sets a float to the wrapped [UBObject]
     */
    fun setFloat(key: String, value: Float) = set(key, UBValueFactory.createFloat32(value))

    /**
     * Sets a double to the wrapped [UBObject]
     */
    fun setDouble(key: String, value: Double) = set(key, UBValueFactory.createFloat64(value))

    /**
     * Sets a [String] to the wrapped [UBObject]
     */
    fun setString(key: String, value: String) = set(key, UBValueFactory.createString(value))

    /**
     * Sets an array of [UBValue]s to the wrapped [UBObject]
     */
    fun setArray(key: String, vararg values: UBValue) = set(key, UBValueFactory.createArray(*values))

    /**
     * Sets an array of [Boolean]s to the wrapped [UBObject]
     */
    fun setBooleanArray(key: String, vararg values: Boolean) = set(key, UBValueFactory.createArray(values))

    /**
     * Sets an array of [Byte]s to the wrapped [UBObject]
     */
    fun setByteArray(key: String, vararg values: Byte) = set(key, UBValueFactory.createArray(values))

    /**
     * Sets an array of [Short]s to the wrapped [UBObject]
     */
    fun setShortArray(key: String, vararg values: Short) = set(key, UBValueFactory.createArray(values))

    /**
     * Sets an array of [Int]s to the wrapped [UBObject]
     */
    fun setIntArray(key: String, vararg values: Int) = set(key, UBValueFactory.createArray(values))

    /**
     * Sets an array of [Long]s to the wrapped [UBObject]
     */
    fun setLongArray(key: String, vararg values: Long) = set(key, UBValueFactory.createArray(values))

    /**
     * Sets an array of [Float]s to the wrapped [UBObject]
     */
    fun setFloatArray(key: String, vararg values: Float) = set(key, UBValueFactory.createArray(values))

    /**
     * Sets an array of [Double]s to the wrapped [UBObject]
     */
    fun setDoubleArray(key: String, vararg values: Double) = set(key, UBValueFactory.createArray(values))

    /**
     * Sets an array of [String]s to the wrapped [UBObject]
     */
    fun setStringArray(key: String, vararg values: String) = set(key, UBValueFactory.createArray(values))

    /**
     * Sets a [UUID] to the wrapped [UBObject]
     */
    fun setUUID(key: String, uuid: UUID) {
        setLong(key + "_m", uuid.mostSignificantBits)
        setLong(key + "_l", uuid.leastSignificantBits)
    }

    /**
     * Sets a null to the wrapped [UBObject]
     */
    fun setNull(key: String) = set(key, UBValueFactory.createNull())

    /**
     * Sets a [UBObject] to the wrapped [UBObject]
     */
    fun setUBObject(key: String, value: UBObject) = set(key, value)

    /**
     * Sets a [UBJ] to the wrapped [UBObject]
     */
    fun setUBObject(key: String, value: UBJ) = setUBObject(key, value.wrappedUBObject)

    /**
     * Tries to create a [UBValue] for this object
     * Will return true if creation was successful and the object was added
     * This should work for most primitive types as well as [java.util.Map]s, [Iterable]s and [UBArray]s
     *
     * @param key Key
     * @param value [Object] value
     * @return True if successful
     */
    fun setObject(key: String, value: Any): Boolean {
        val ubValue = UBValueFactory.createValue(value)
        val isNotNull = ubValue != null
        if (isNotNull)
            set(key, ubValue)
        return isNotNull
    }

    /**
     * Gets a generic [UBValue] from [UBJ.wrappedUBObject]
     * Used internally in this class only
     */
    private fun get(key: String): UBValue? = wrappedUBObject[key]

    /**
     * Returns true if the key-value pair does not exist, or the key is mapped to null
     */
    fun isNull(key: String): Boolean {
        val value = get(key)
        return value == null || value.isNull
    }

    /**
     * Gets a [Boolean] from the wrapped [UBObject]
     * Returns null if the value was not a boolean or the key-value pair did not exist
     */
    fun getBoolean(key: String): Boolean? {
        val value = get(key)
        return if (value!!.isBool) value.asBool() else null
    }

    /**
     * Gets a boolean from the wrapped [UBObject]
     * If the value was null, then will return false
     */
    fun getBooleanSafe(key: String): Boolean = getBoolean(key) ?: false

    /**
     * Gets a [Byte] from the wrapped [UBObject]
     * Returns null if the value was not a number or the key-value pair did not exist
     */
    fun getByte(key: String): Byte? {
        val value = get(key)
        return if (value != null && value.isNumber) value.asByte() else null
    }

    /**
     * Gets a byte from the wrapped [UBObject]
     * If the value was null, then will return 0
     */
    fun getByteSafe(key: String): Byte = getByte(key) ?: 0

    /**
     * Gets a [Short] from the wrapped [UBObject]
     * Returns null if the value was not a number or the key-value pair did not exist
     */
    fun getShort(key: String): Short? {
        val value = get(key)
        return if (value != null && value.isNumber) value.asShort() else null
    }

    /**
     * Gets a short from the wrapped [UBObject]
     * If the value was null, then will return 0
     */
    fun getShortSafe(key: String): Short = getShort(key) ?: 0

    /**
     * Gets a [Integer] from the wrapped [UBObject]
     * Returns null if the value was not a number or the key-value pair did not exist
     */
    fun getInt(key: String): Int? {
        val value = get(key)
        return if (value != null && value.isNumber) value.asInt() else null
    }

    /**
     * Gets an integer from the wrapped [UBObject]
     * If the value was null, then will return 0
     */
    fun getIntSafe(key: String): Int = getInt(key) ?: 0

    /**
     * Gets a [Long] from the wrapped [UBObject]
     * Returns null if the value was not a number or the key-value pair did not exist
     */
    fun getLong(key: String): Long? {
        val value = get(key)
        return if (value != null && value.isNumber) value.asLong() else null
    }

    /**
     * Gets a long from the wrapped [UBObject]
     * If the value was null, then will return 0
     */
    fun getLongSafe(key: String): Long = getLong(key) ?: 0

    /**
     * Gets a [Float] from the wrapped [UBObject]
     * Returns null if the value was not a number or the key-value pair did not exist
     */
    fun getFloat(key: String): Float? {
        val value = get(key)
        return if (value != null && value.isNumber) value.asFloat32() else null
    }

    /**
     * Gets a float from the wrapped [UBObject]
     * If the value was null, then will return 0
     */
    fun getFloatSafe(key: String): Float = getFloat(key) ?: 0F

    /**
     * Gets a [Double] from the wrapped [UBObject]
     * Returns null if the value was not a number or the key-value pair did not exist
     */
    fun getDouble(key: String): Double? {
        val value = get(key)
        return if (value != null && value.isNumber) value.asFloat64() else null
    }

    /**
     * Gets a double from the wrapped [UBObject]
     * If the value was null, then will return 0
     */
    fun getDoubleSafe(key: String): Double = getDouble(key) ?: 0.0

    /**
     * Gets a [String] from the wrapped [UBObject]
     * Returns null if the value was not a number or the key-value pair did not exist
     */
    fun getString(key: String): String? {
        val value = get(key)
        return when {
            value == null -> null
            value.isString -> value.asString()
            value.isNumber -> value.asLong().toString()
            else -> null
        }
    }

    /**
     * Gets a [String] from the wrapped [UBObject]
     * If the value was null, then will return an empty String
     */
    fun getStringSafe(key: String): String = getString(key) ?: ""

    /**
     * Gets a [UBArray] from the wrapped [UBObject]
     * Returns null if the value was not an array or the key-value pair did not exist
     */
    fun getArray(key: String): UBArray? {
        val value = get(key)
        return if (value != null && value.isArray) value.asArray() else null
    }

    /**
     * Gets a [UBArray] from the wrapped [UBObject]
     * If the value was null, then will return an empty [UBArray]
     */
    fun getArraySafe(key: String): UBArray = getArray(key) ?: UBValueFactory.createArray()

    /**
     * Gets a [BooleanArray] from the wrapped [UBObject]
     * Returns null if the value was not an array, the key-value pair did not exist or the array type was not a [Boolean]
     */
    fun getBooleanArray(key: String): BooleanArray? {
        val array = getByteArray(key)
        return if (array != null) BooleanArray(array.size) { i -> array[i] == 1.toByte() } else null
    }

    /**
     * Gets a [BooleanArray] from the wrapped [UBObject]
     * If the value was null, then will return an empty [BooleanArray]
     */
    fun getBooleanArraySafe(key: String): BooleanArray = getBooleanArray(key) ?: booleanArrayOf()

    /**
     * Gets a [ByteArray] from the wrapped [UBObject]
     * Returns null if the value was not an array, the key-value pair did not exist or the array type was not a [Byte]
     */
    fun getByteArray(key: String): ByteArray? {
        val array = getArray(key)
        return if (array != null && array.strongType == UBArray.ArrayType.Int8) (array as UBInt8Array).values else null
    }

    /**
     * Gets a [ByteArray] from the wrapped [UBObject]
     * If the value was null, then will return an empty [ByteArray]
     */
    fun getByteArraySafe(key: String): ByteArray = getByteArray(key) ?: byteArrayOf()

    /**
     * Gets a [ShortArray] from the wrapped [UBObject]
     * Returns null if the value was not an array, the key-value pair did not exist or the array type was not a [Short]
     */
    fun getShortArray(key: String): ShortArray? {
        val array = getArray(key)
        return if (array != null && array.strongType == UBArray.ArrayType.Int16) (array as UBInt16Array).values else null
    }

    /**
     * Gets a [ShortArray] from the wrapped [UBObject]
     * If the value was null, then will return an empty [ShortArray]
     */
    fun getShortArraySafe(key: String): ShortArray = getShortArray(key) ?: shortArrayOf()

    /**
     * Gets a [IntArray] from the wrapped [UBObject]
     * Returns null if the value was not an array, the key-value pair did not exist or the array type was not an [Int]
     */
    fun getIntArray(key: String): IntArray? {
        val array = getArray(key)
        return if (array != null && array.strongType == UBArray.ArrayType.Int32) (array as UBInt32Array).values else null
    }

    /**
     * Gets a [IntArray] from the wrapped [UBObject]
     * If the value was null, then will return an empty [IntArray]
     */
    fun getIntArraySafe(key: String): IntArray = getIntArray(key) ?: intArrayOf()

    /**
     * Gets a [LongArray] from the wrapped [UBObject]
     * Returns null if the value was not an array, the key-value pair did not exist or the array type was not a [Long]
     */
    fun getLongArray(key: String): LongArray? {
        val array = getArray(key)
        return if (array != null && array.strongType == UBArray.ArrayType.Int64) (array as UBInt64Array).values else null
    }

    /**
     * Gets a [LongArray] from the wrapped [UBObject]
     * If the value was null, then will return an empty [LongArray]
     */
    fun getLongArraySafe(key: String): LongArray = getLongArray(key) ?: longArrayOf()

    /**
     * Gets a [FloatArray] from the wrapped [UBObject]
     * Returns null if the value was not an array, the key-value pair did not exist or the array type was not a [Float]
     */
    fun getFloatArray(key: String): FloatArray? {
        val array = getArray(key)
        return if (array != null && array.strongType == UBArray.ArrayType.Float32) (array as UBFloat32Array).values else null
    }

    /**
     * Gets a [FloatArray] from the wrapped [UBObject]
     * If the value was null, then will return an empty [FloatArray]
     */
    fun getFloatArraySafe(key: String): FloatArray = getFloatArray(key) ?: floatArrayOf()

    /**
     * Gets a [DoubleArray] from the wrapped [UBObject]
     * Returns null if the value was not an array, the key-value pair did not exist or the array type was not a [Double]
     */
    fun getDoubleArray(key: String): DoubleArray? {
        val array = getArray(key)
        return if (array != null && array.strongType == UBArray.ArrayType.Float64) (array as UBFloat64Array).values else null
    }

    /**
     * Gets a [DoubleArray] from the wrapped [UBObject]
     * If the value was null, then will return an empty [DoubleArray]
     */
    fun getDoubleArraySafe(key: String): DoubleArray = getDoubleArray(key) ?: doubleArrayOf()

    /**
     * Gets a [String] [Array] from the wrapped [UBObject]
     * Returns null if the value was not an array, the key-value pair did not exist or the array type was not a [String]
     */
    fun getStringArray(key: String): Array<String>? {
        val array = getArray(key)
        return if (array != null && array.strongType == UBArray.ArrayType.String) (array as UBStringArray).values else null
    }

    /**
     * Gets a [String] [Array] from the wrapped [UBObject]
     * If the value was null, then will return an empty [String] [Array]
     */
    fun getStringArraySafe(key: String): Array<String> = getStringArray(key) ?: arrayOf()

    /**
     * Gets a [UUID] from the wrapped [UBObject]
     * Returns null if either of the component parts of the UUID do not exist
     */
    fun getUUID(key: String): UUID? {
        val most = getLong(key + "_m")
        val least = getLong(key + "_l")
        return if (most != null && least != null) UUID(most, least) else null
    }

    /**
     * Gets a [UBObject] from the wrapped [UBObject]
     * Returns null if the value was not an object or the key-value pair did not exist
     */
    fun getUBObject(key: String): UBObject? {
        val value = get(key)
        return if (value != null && value.isObject) value.asObject() else null
    }

    /**
     * Gets a [UBJ] from the wrapped [UBObject]
     * Returns null if the value was not an object or the key-value pair did not exist
     */
    fun getUBObjectWrapped(key: String): UBJ? {
        val value = getUBObject(key)
        return if (value != null) UBJ(value) else null
    }

    /**
     * Gets a [UBArray] from the wrapped [UBObject]
     * Returns null if the value was not an array or the key-value pair did not exist
     */
    fun getUBArray(key: String): UBArray? {
        val value = get(key)
        return if (value != null && value.isArray) value.asArray() else null
    }

    override fun hashCode(): Int = Objects.hash(wrappedUBObject)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (other !is UBJ) return false
        val o = other as UBJ?
        return wrappedUBObject == o!!.wrappedUBObject
    }

    companion object {
        /**
         * Helper method to create a [UBJ] from a [UBValue] if possible
         * Returns null if the value is not a [UBObject]
         *
         * @param ubValue Value to try use as a UBObject
         * @return [UBJ] or null
         */
        fun create(ubValue: UBValue): UBJ? = if (ubValue.isObject) UBJ(ubValue.asObject()) else null
    }
}
