package bvanseg.kcommons.ubjson

import com.devsmart.ubjson.UBArray
import com.devsmart.ubjson.UBObject
import com.devsmart.ubjson.UBValue
import com.devsmart.ubjson.UBValueFactory
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
