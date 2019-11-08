package bvanseg.kotlincommons.graphics

import bvanseg.kotlincommons.comparable.clamp
import java.io.Serializable

/**
 * A simpler implementation of [java.awt.Color] that provides additional functionality.
 *
 * @author Boston Vanseghi
 * @since 2.0.2
 */
class Color: Serializable {

    var iRed: Int = 0
        set(value) {
            fRed = clamp(value / 255f, 0f, 1f)
            field = value
        }
    var fRed: Float = 0f
        set(value) {
            iRed = clamp((value * 255).toInt(), 0, 255)
            field = value
        }

    var iGreen: Int = 0
        set(value) {
            fGreen = clamp(value / 255f, 0f, 1f)
            field = value
        }
    var fGreen: Float = 0f
        set(value) {
            iGreen = clamp((value * 255).toInt(), 0, 255)
            field = value
        }

    var iBlue: Int = 0
        set(value) {
            fBlue = clamp(value / 255f, 0f, 1f)
            field = value
        }
    var fBlue: Float = 0f
        set(value) {
            iBlue = clamp((value * 255).toInt(), 0, 255)
            field = value
        }

    var iAlpha: Int = 0
        set(value) {
            fAlpha = clamp(value / 255f, 0f, 1f)
            field = value
        }
    var fAlpha: Float = 0f
        set(value) {
            iAlpha = clamp((value * 255).toInt(), 0, 255)
            field = value
        }

    private var usesAlpha = false

    constructor(color: Int, hasAlpha: Boolean = false) {
        this.usesAlpha = hasAlpha
        if(hasAlpha) {
            iRed = (color shr 24) and 0xFF
            iGreen = (color shr 16) and 0xFF
            iBlue = (color shr 8) and 0xFF
            iAlpha = color and 0xFF
        }else {
            iRed = (color shr 16) and 0xFF
            iGreen = (color shr 8) and 0xFF
            iBlue = color and 0xFF
        }
    }

    constructor(red: Int, green: Int, blue: Int, alpha: Int = 0xFF) {
        this.iRed = red
        this.iGreen = green
        this.iBlue = blue
        this.iAlpha = alpha
    }

    constructor(red: Float, green: Float, blue: Float, alpha: Float = 1.0f) {
        this.iRed = (255 * red).toInt()
        this.iGreen = (255 * green).toInt()
        this.iBlue = (255 * blue).toInt()
        this.iAlpha = (255 * alpha).toInt()
    }

    /**
     * Applies shading to the [Color].
     *
     * @param shadeFactor - A factor to shade the color by. Should have a value between 0 and 1.
     *
     * @return The shaded [Color].
     */
    fun shade(shadeFactor: Float): Color = this.apply {
        fRed *= (1 - shadeFactor)
        fGreen *= fGreen * (1 - shadeFactor)
        fBlue *= fBlue * (1 - shadeFactor)
    }


    /**
     * Applies a tint to the [Color].
     *
     * @param tintFactor - A factor to tint the color by. Should have a value between 0 and 1.
     *
     * @return The tinted [Color].
     */
    fun tint(tintFactor: Float): Color = this.apply {
        fRed += (255 - fRed) * tintFactor
        fGreen += (255 - fGreen) * tintFactor
        fBlue += (255 - fBlue) * tintFactor
    }

    /**
     * Retrieves the color as an [Int]
     *
     * @return an [Int] of the color.
     */
    fun toInt(): Int = if(usesAlpha) (iRed shl 24) + (iGreen shl 16) + (iBlue shl 8) + iAlpha else (iRed shl 16) + (iGreen shl 8) + iBlue

    /** OPERATORS **/

    operator fun plus(otherColor: Color) = this.apply {
        iRed += otherColor.iRed
        iGreen += otherColor.iGreen
        iBlue += otherColor.iBlue
    }

    operator fun minus(otherColor: Color) = this.apply {
        iRed -= otherColor.iRed
        iGreen -= otherColor.iGreen
        iBlue -= otherColor.iBlue
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