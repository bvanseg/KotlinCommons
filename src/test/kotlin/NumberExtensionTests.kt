import bvanseg.kotlincommons.util.comparable.clamp
import org.junit.jupiter.api.Test

class NumberExtensionTests {

    @Test
    fun intClamp() {
        var num = 1000
        num = num.clamp(0, 100) // Clamps the number.
        assert(num == 100)
    }

    @Test
    fun floatClamp() {
        var num = 0.86F
        num = num.clamp(0.2313F, 0.731741F) // Clamps the number.
        assert(num == 0.731741F)
    }
}