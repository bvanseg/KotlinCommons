package bvanseg.kotlincommons.graphics

import bvanseg.kotlincommons.tuples.Quad
import java.io.Serializable
import kotlin.math.round

/**
 * A cleaner (yet more advanced) implementation of [java.awt.Color] that provides additional functionality.
 *
 * @author Boston Vanseghi
 * @since 2.0.2
 */
class Color: Serializable {

    var color: Int = 0

    constructor(color: Int, alpha: Int = 0xFF) {
        this.color = color
        this.color = this.color or (alpha shl 24)
    }

    constructor(red: Int, green: Int, blue: Int, alpha: Int = 0xFF) {
        this.color = this.color or (red shl 16) or (green shl 8) or blue or (alpha shl 24)
    }

    constructor(red: Float, green: Float, blue: Float, alpha: Float = 1.0f) {
        this.color = this.color or ((255 * red).toInt() shl 16) or ((255 * green).toInt() shl 8) or (255 * blue).toInt() or ((255 * alpha).toInt() shl 24)
    }

    /**
     * Applies shading to the [Color].
     *
     * @param shadeFactor - A factor to shade the color by. Should have a value between 0 and 1.
     *
     * @return The shaded [Color].
     */
    fun shade(shadeFactor: Float): Color = this.apply {
        var red: Int = (color shr 16) and 0xFF
        var green: Int = (color shr 8) and 0xFF
        var blue: Int = color and 0xFF
        red = (red * (1 - shadeFactor)).toInt()
        green = (green * (1 - shadeFactor)).toInt()
        blue = (blue * (1 - shadeFactor)).toInt()
        color = 0 or (red shl 16) or (green shl 8) or blue or (getAlpha())
    }


    /**
     * Applies a tint to the [Color].
     *
     * @param tintFactor - A factor to tint the color by. Should have a value between 0 and 1.
     *
     * @return The tinted [Color].
     */
    fun tint(tintFactor: Float): Color = this.apply {
        var red: Int = (color shr 16) and 0xFF
        var green: Int = (color shr 8) and 0xFF
        var blue: Int = color and 0xFF
        red = (red * tintFactor).toInt()
        green = (green * tintFactor).toInt()
        blue = (blue * tintFactor).toInt()
        color = 0 or (red shl 16) or (green shl 8) or blue or (getAlpha())
    }

    /** OPERATORS **/

    operator fun plus(otherColor: Color) = this.apply {
        color += otherColor.color
    }

    operator fun minus(otherColor: Color) = this.apply {
        color -= otherColor.color
    }

    fun getAlpha(): Int = (color shr 24) and 0xFF
    fun getRed(): Int = (color shr 16) and 0xFF
    fun getGreen(): Int = (color shr 8) and 0xFF
    fun getBlue(): Int = color and 0xFF
    fun getRGB(): Triple<Int, Int, Int> = Triple(getRed(), getGreen(), getBlue())
    fun getRGBA(): Quad<Int, Int, Int, Int> = Quad(getRed(), getGreen(), getBlue(), getAlpha())
    fun getHSL(): Triple<Int, Int, Int> = Triple(getHue(), getSaturation(), getLuminance())
    fun getHSLA(): Quad<Int, Int, Int, Int> = Quad(getHue(), getSaturation(), getLuminance(), getAlpha())

    fun setAlpha(value: Int) {
        this.color = 0 or (getRed() shl 16) or (getGreen() shl 8) or getBlue() or (value shl 24)
    }

    fun setRed(value: Int) {
        this.color = 0 or (value shl 16) or (getGreen() shl 8) or getBlue() or (getAlpha() shl 24)
    }

    fun setGreen(value: Int) {
        this.color = 0 or (getRed() shl 16) or (value shl 8) or getBlue() or (getAlpha() shl 24)
    }

    fun setBlue(value: Int) {
        this.color = 0 or (getRed() shl 16) or (getGreen() shl 8) or value or (getAlpha() shl 24)
    }

    fun getLuminance(): Int {
        val r = getRed() / 255f
        val g = getGreen() / 255f
        val b = getBlue() / 255f

        val max = maxOf(r, g, b)
        val min = minOf(r, g, b)

        return round(((max + min) / 2f) * 100f).toInt()
    }

    fun getSaturation(): Int {
        val r = getRed() / 255f
        val g = getGreen() / 255f
        val b = getBlue() / 255f
        val luminance = getLuminance()

        val max = maxOf(r, g, b)
        val min = minOf(r, g, b)

        return when {
            luminance / 100f < 1 -> round(((max-min)/(1 - (2 * (getLuminance()/100f) - 1))) * 100).toInt()
            else -> 0
        }
    }

    fun getHue(): Int {
        val r = getRed() / 255f
        val g = getGreen() / 255f
        val b = getBlue() / 255f

        when {
            r > g && g >= b -> return round((((g-b)/(r-b))) * 60).toInt()
            g > r && r >= b -> return round((2.0 - ((r-b)/(g-b))) * 60).toInt()
            g >= b && b > r -> return round((2.0 + ((b-r)/(g-r))) * 60).toInt()
            b > g && g > r -> return round((4.0 - ((g-r)/(b-r))) * 60).toInt()
            b > r && r >= g -> return round((4.0 + ((r-g)/(b-g))) * 60).toInt()
            r >= b && b > g -> return round((6.0 - ((b-g)/(r-g))) * 60).toInt()
        }

        return -1
    }

    @Suppress("unused")
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
        val DARK_SLATE_GRAY by lazy { Color(0x2F4F4F) }
        val DARK_SLATE_GREY by lazy { Color(0x2F4F4F) }
        val DARK_TURQUOISE by lazy { Color(0x00CED1) }
        val DARK_VIOLET by lazy { Color(0x9400D3) }
        val DEEP_PINK by lazy { Color(0xFF1493) }
        val DEEP_SKY_BLUE by lazy { Color(0x00BFFF) }
        val DIM_GRAY by lazy { Color(0x696969) }
        val DIM_GREY by lazy { Color(0x696969) }
        val DODGER_BLUE by lazy { Color(0x1E90FF) }

        val FIRE_BRICK by lazy { Color(0xB22222) }
        val FLORAL_WHITE by lazy { Color(0xFFFAF0) }
        val FOREST_GREEN by lazy { Color(0x228B22) }
        val FUCHSIA by lazy { Color(0xFF00FF) }

        val GAINSBORO by lazy { Color(0xDCDCDC) }
        val GHOST_WHITE by lazy { Color(0xF8F8FF) }
        val GOLD by lazy { Color(0xFFD700) }
        val GOLDEN_ROD by lazy { Color(0xDAA520) }
        val GRAY by lazy { Color(0x808080) }
        val GREY by lazy { Color(0x808080) }
        val GREEN_YELLOW by lazy { Color(0xADFF2F) }

        val HONEY_DEW by lazy { Color(0xF0FFF0) }
        val HOT_PINK by lazy { Color(0xFF89B4) }

        val INDIAN_RED by lazy { Color(0xCD5C5C) }
        val INDIGO by lazy { Color(0x4B0082) }
        val IVORY by lazy { Color(0xFFFFF0) }

        val KHAKI by lazy { Color(0xF0E68C) }

        val LAVENDER by lazy { Color(0xE6E6FA) }
        val LAVENDER_BLUSH by lazy { Color(0xFFF0F5) }
        val LAWN_GREEN by lazy { Color(0x7CFC00) }
        val LEMMON_CHIFFON by lazy { Color(0xFFFACD) }
        val LIGHT_BLUE by lazy { Color(0xADD8E6) }
        val LIGHT_CORAL by lazy { Color(0xF08080) }
        val LIGHT_CYAN by lazy { Color(0xE0FFFF) }
        val LIGHT_GOLDEN_ROD_YELLOW by lazy { Color(0xFAFAD2) }
        val LIGHT_GRAY by lazy { Color(0xD3D3D3) }
        val LIGHT_GREY by lazy { Color(0xD3D3D3) }
        val LIGHT_GREEN by lazy { Color(0x90EE90) }
        val LIGHT_PINK by lazy { Color(0xFFB6C1) }
        val LIGHT_SALMON by lazy { Color(0xFFA07A) }
        val LIGHT_SEA_GREEN by lazy { Color(0x20B2AA) }
        val LIGHT_SKY_BLUE by lazy { Color(0x87CEFA) }
        val LIGHT_SLATE_GRAY by lazy { Color(0x778899) }
        val LIGHT_SLATE_GREY by lazy { Color(0x778899) }
        val LIGHT_STEEL_BLUE by lazy { Color(0xB0C4DE) }
        val LIGHT_YELLOW by lazy { Color(0xFFFFE0) }
        val LIME by lazy { Color(0x00FF00) }
        val LIME_GREEN by lazy { Color(0x32CD32) }
        val LINEN by lazy { Color(0xFAF0E6) }

        val MAGENTA by lazy { Color(0xFF00FF) }
        val MAROON by lazy { Color(0x800000) }
        val MEDIUM_AQUA_MARINE by lazy { Color(0x66CDAA) }
        val MEDIUM_BLUE by lazy { Color(0x0000CD) }
        val MEDIUM_ORCHID by lazy { Color(0xBA55D3) }
        val MEDIUM_PURPLE by lazy { Color(0x9370DB) }
        val MEDIUM_SEA_GREEN by lazy { Color(0x3CB371) }
        val MEDIUM_SLATE_BLUE by lazy { Color(0x7B68EE) }
        val MEDIUM_SPRING_GREEN by lazy { Color(0x00FA9A) }
        val MEDIUM_TURQUOISE by lazy { Color(0x48D1CC) }
        val MEDIUM_VIOLET_RED by lazy { Color(0xC71585) }
        val MIDNIGHT_BLUE by lazy { Color(0x191970) }
        val MINT_CREAM by lazy { Color(0xF5FFFA) }
        val MISTY_ROSE by lazy { Color(0xFFE4E1) }
        val MOCCASIN by lazy { Color(0xFFE4B5) }

        val NAVAJO_WHITE by lazy { Color(0xFFDEAD) }
        val NAVY by lazy { Color(0x000080) }

        val OLD_LACE by lazy { Color(0xFDF5E6) }
        val OLIVE by lazy { Color(0x808000) }
        val OLIVE_DRAB by lazy { Color(0x6B8E23) }
        val ORANGE by lazy { Color(0xFFA500) }
        val ORANGE_RED by lazy { Color(0xFF4500) }
        val ORCHID by lazy { Color(0xDA70D6) }

        val PALE_GOLDEN_ROD by lazy { Color(0xEEE8AA) }
        val PALE_GREEN by lazy { Color(0x98FB98) }
        val PALE_TURQUOISE by lazy { Color(0xAFEEEE) }
        val PALE_VIOLET_RED by lazy { Color(0xDB7093) }
        val PAPAYA_WHIP by lazy { Color(0xFFEFD5) }
        val PEACH_PUFF by lazy { Color(0xFFDAB9) }
        val PERU by lazy { Color(0xCD853F) }
        val PINK by lazy { Color(0xFFC0CB) }
        val PLUM by lazy { Color(0xDDA0DD) }
        val POWDER_BLUE by lazy { Color(0xB0E0E6) }
        val PURPLE by lazy { Color(0x800080) }

        val REBECCA_PURPLE by lazy { Color(0x663399) }
        val ROSY_BROWN by lazy { Color(0xBC8F8F) }
        val ROYAL_BLUE by lazy { Color(0x4169E1) }

        val SADDLE_BROWN by lazy { Color(0x8B4513) }
        val SALMON by lazy { Color(0xFA8072) }
        val SANDY_BROWN by lazy { Color(0xF4A460) }
        val SEA_GREEN by lazy { Color(0x2E8B57) }
        val SEA_SHELL by lazy { Color(0xFFF5EE) }
        val SIENNA by lazy { Color(0xA0522D) }
        val SILVER by lazy { Color(0xC0C0C0) }
        val SKY_BLUE by lazy { Color(0x87CEEB) }
        val SLATE_BLUE by lazy { Color(0x6A5ACD) }
        val SLATE_GRAY by lazy { Color(0x708090) }
        val SLATE_GREY by lazy { Color(0x708090) }
        val SNOW by lazy { Color(0xFFFAFA) }
        val SPRING_GREEN by lazy { Color(0x00FF7F) }
        val STEEL_BLUE by lazy { Color(0x4682B4) }

        val TAN by lazy { Color(0xD2B48C) }
        val TEAL by lazy { Color(0x008080) }
        val THISTLE by lazy { Color(0xD8BFD8) }
        val TOMATO by lazy { Color(0xFF6347) }
        val TURQUOISE by lazy { Color(0x40E0D0) }

        val VIOLET by lazy { Color(0xEE82EE) }

        val WHEAT by lazy { Color(0xF5DEB3) }
        val WHITE_SMOKE by lazy { Color(0xF5F5F5) }

        val YELLOW by lazy { Color(0xFFFF00) }
        val YELLOW_GREEN by lazy { Color(0x9ACD32) }
    }
}