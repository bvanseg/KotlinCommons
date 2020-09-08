package bvanseg.kotlincommons

import bvanseg.kotlincommons.prettyprinter.PrettyColors
import bvanseg.kotlincommons.prettyprinter.buildPrettyString
import org.junit.jupiter.api.Test

class PrettyPrinterTest{
    @ExperimentalStdlibApi
    @Test
    fun appendTest(){
        val string = buildPrettyString {
            append("Hello, world!")
        }
    }

    @ExperimentalStdlibApi
    @Test
    fun appendWithNewLineTest(){
        val string = buildPrettyString {
            appendWithNewLine("Hello, world!")
        }
    }

    @ExperimentalStdlibApi
    @Test
    fun simpleColorTest(){
        val string = buildPrettyString {
            red{
                appendWithNewLine("Hello, world!")
            }
            blue{
                appendWithNewLine("Hello, world!")
            }
            green{
                appendWithNewLine("Hello, world!")
            }
            yellow{
                appendWithNewLine("Hello, world!")
            }
            white{
                appendWithNewLine("Hello, world!")
            }
            black{
                appendWithNewLine("Hello, world!")
            }
            underline{
                appendWithNewLine("Hello, world!")
            }
            bold{
                appendWithNewLine("Hello, world!")
            }
            italics{
                appendWithNewLine("Hello, world!")
            }
        }
        val target =
            "${PrettyColors.RED.ansi}Hello, world!\n${PrettyColors.RESET.ansi}" +
            "${PrettyColors.BLUE.ansi}Hello, world!\n${PrettyColors.RESET.ansi}" +
            "${PrettyColors.GREEN.ansi}Hello, world!\n${PrettyColors.RESET.ansi}" +
            "${PrettyColors.YELLOW.ansi}Hello, world!\n${PrettyColors.RESET.ansi}" +
            "${PrettyColors.WHITE.ansi}Hello, world!\n${PrettyColors.RESET.ansi}" +
            "${PrettyColors.BLACK.ansi}Hello, world!\n${PrettyColors.RESET.ansi}" +
            "${PrettyColors.UNDERLINE.ansi}Hello, world!\n${PrettyColors.RESET.ansi}" +
            "${PrettyColors.BOLD.ansi}Hello, world!\n${PrettyColors.RESET.ansi}" +
            "${PrettyColors.ITALIC.ansi}Hello, world!\n${PrettyColors.RESET.ansi}"

        assert(string == target)
    }
}