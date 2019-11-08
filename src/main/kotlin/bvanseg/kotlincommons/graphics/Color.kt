package bvanseg.kotlincommons.graphics

import java.io.Serializable

/**
 * A cleaner (yet more advanced) implementation of [java.awt.Color] that provides additional functionality.
 *
 * @author Boston Vanseghi
 * @since 2.0.2
 */
class Color: Serializable {

    var color: Int = 0

    private var usesAlpha = false

    constructor(color: Int, hasAlpha: Boolean = false) {
        this.usesAlpha = hasAlpha
        this.color = color
    }

    constructor(red: Int, green: Int, blue: Int, alpha: Int = 0xFF) {

        this.color += red shl 24
        this.color += green shl 16
        this.color += blue shl 8
        this.color += alpha
    }

    constructor(red: Float, green: Float, blue: Float, alpha: Float = 1.0f) {
        this.color += (255 * red).toInt()
        this.color += (255 * green).toInt()
        this.color = (255 * blue).toInt()
        this.color += (255 * alpha).toInt()
    }

    /**
     * Applies shading to the [Color].
     *
     * @param shadeFactor - A factor to shade the color by. Should have a value between 0 and 1.
     *
     * @return The shaded [Color].
     */
    fun shade(shadeFactor: Float): Color = this.apply {
        var red: Int = (color shr 24) and 0xFF
        var green: Int = (color shr 16) and 0xFF
        var blue: Int = (color shr 8) and 0xFF
        red = (red * (1 - shadeFactor)).toInt()
        green = (green * (1 - shadeFactor)).toInt()
        blue = (blue * (1 - shadeFactor)).toInt()
        color += red shl 24
        color += green shl 16
        color += blue shl 8
    }


    /**
     * Applies a tint to the [Color].
     *
     * @param tintFactor - A factor to tint the color by. Should have a value between 0 and 1.
     *
     * @return The tinted [Color].
     */
    fun tint(tintFactor: Float): Color = this.apply {
        var red: Int = (color shr 24) and 0xFF
        var green: Int = (color shr 16) and 0xFF
        var blue: Int = (color shr 8) and 0xFF
        red = (red * tintFactor).toInt()
        green = (green * tintFactor).toInt()
        blue = (blue * tintFactor).toInt()
        color += red shl 24
        color += green shl 16
        color += blue shl 8
    }

    /** OPERATORS **/

    operator fun plus(otherColor: Color) = this.apply {
        color + otherColor.color
    }

    operator fun minus(otherColor: Color) = this.apply {
        color - otherColor.color
    }

    /** COLOR MANIPULATION **/
    fun getRed(): Int = (color shr 24) and 0xFF
    fun getGreen(): Int = (color shr 16) and 0xFF
    fun getBlue(): Int = (color shr 8) and 0xFF
    fun getAlpha(): Int = color and 0xFF

    fun setRed(value: Int) {
        this.color += value shl 24
    }

    fun setGreen(value: Int) {
        this.color += value shl 16
    }

    fun setBlue(value: Int) {
        this.color += value shl 8
    }

    fun setAlpha(value: Int) {
        this.color += value
    }

    companion object {
        val BLACK by lazy { Color(0, 0, 0) }
        val WHITE by lazy { Color(0xFF, 0xFF, 0xFF) }

        val RED by lazy { Color(0xFF, 0, 0) }
        val GREEN by lazy { Color(0, 0xFF, 0) }
        val BLUE by lazy { Color(0, 0, 0xFF) }

        /** BROWSER-COMPLIANT COLORS **/
        val ALICE_BLUE by lazy { Color(0xF0F8FF) }
        val ANTIQUE_WHITE by lazy { Color(0xFAEBD7) }
        val AQUA by lazy { Color(0x00FFFF) }
        val AQUAMARINE by lazy { Color(0x7FFFD4) }
        val AZURE by lazy { Color(0xF0FFFF) }

        val BEIGE by lazy { Color(0xF5F5DC) }
        val BISQUE by lazy { Color(0xFFE4C4) }
        val BLANCHED_ALMOND by lazy { Color(0xFFEBCD) }
        val BLUE_VIOLET by lazy { Color(0x8A2BE2) }
        val BROWN by lazy { Color(0xA52A2A) }
        val BURLY_WOOD by lazy { Color(0xDEB887) }

        val CADET_BLUE by lazy { Color(0x5F9EA0) }
        val CHARTREUSE by lazy { Color(0x7FFF00) }
        val CHOCOLATE by lazy { Color(0xD2691E) }
        val CORAL by lazy { Color(0xFF7F50) }
        val CORNFLOWER_BLUE by lazy { Color(0x6495ED) }
        val CORNSILK by lazy { Color(0xFFF8DC) }
        val CRIMSON by lazy { Color(0xDC143C) }
        val CYAN by lazy { Color(0x00FFFF) }

        val DARK_BLUE by lazy { Color(0x0000B8) }
        val DARK_CYAN by lazy { Color(0x008B8B) }
        val DARK_GOLDEN_ROD by lazy { Color(0xB8860B) }
        val DARK_GRAY by lazy { Color(0xA9A9A9) }
        val DARK_GREY by lazy { Color(0xA9A9A9) }
        val DARK_GREEN by lazy { Color(0x006400) }
        val DARK_KHAKI by lazy { Color(0xBDB76B) }
        val DARK_MAGENTA by lazy { Color(0x8B008B) }
        val DARK_OLIVE_GREEN by lazy { Color(0x556B2F) }
        val DARK_ORANGE by lazy { Color(0xFF8C00) }
        val DARK_ORCHID by lazy { Color(0x9932CC) }
        val DARK_RED by lazy { Color(0x8B0000) }
        val DARK_SALMON by lazy { Color(0xE9967A) }
        val DARK_SEA_GREEN by lazy { Color(0x8FBC8F) }
        val DARK_SLATE_BLUE by lazy { Color(0x8A2BE2) }
    }
}