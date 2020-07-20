package bvanseg.kotlincommons.prettyprinter

/**
 * ANSI color codes, used in the DSL for coloring strings.
 */
enum class PrettyColors(val ansi: String){
    RED("\u001B[31m"),
    BLUE("\u001B[34m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    WHITE("\u001B[30m"),
    BLACK("\u001B[37m"),
    UNDERLINE("\u001B[04m"),
    BOLD("\u001B[01m"),
    ITALIC("\u001B[03m"),
    RESET("\u001B[0m")
    ;
}

/**
 * Allows for easily readable, easily maintable pretty strings to be used for any purpose.
 *
 * When appending to the buffer using the default `append(String)`, it will check if we have any indentation levels.
 * When we have an indentation level, it will check if there are newlines in the appendee to then adjust each line
 * to be indented. This means that appending previously indented, with or without lines, will be indented to match
 * the rest of the string.
 *
 * If we have a string called 'a' and a string called 'b', then appending 'b' to 'a' would allow us to produce the following result:
 *
 * ```
 *  val b = buildPrettyString{
 *      appendWithNewLine("This is a string in 'b'")
 *      indent{
 *          appendWithNewLine("This is an indented string in 'b'")
 *      }
 *  }
 *  val a = buildPrettyString{
 *      appendWithNewLine("Hello, world!")
 *      indent{
 *          appendWithNewLine("This is an indented string")
 *          append(b)
 *      }
 *  }
 * ```
 * Result:
 * ```
 *  Hello, world!
 *      This is an indented string
 *      This is a string in 'b'
 *          This is an indented string in 'b'
 * ```
 * This allows us to keep all formatting such that any substrings with formatting, such as indentation, are matched
 * to fit the rest of the superstring.
 *
 * We also have some helper indentations such that we may indent an N number of times.
 * ```
 *  buildPrettyString{
 *      indentN(5){
 *          append("This is indented 5 times")
 *      }
 *  }
 *  //                      This is indented 5 times
 * ```
 * We can also add colors and styles.
 * ```
 *  buildPrettyString{
 *      red{
 *          append("This is red")
 *      }
 *      blue{
 *          append("This is blue")
 *      }
 *      green{
 *          append("This is green")
 *      }
 *      yellow{
 *          append("This is yellow")
 *      }
 *      white{
 *          append("This is white")
 *      }
 *      black{
 *          append("This is black")
 *      }
 *      bold{
 *          append("This is bold")
 *      }
 *      italics{
 *          append("This is italicized")
 *      }
 *      underline{
 *          append("This is underlined")
 *      }
 *  }
 * ```
 * We can also add custom spacing to format tediously and uniquely.
 * ```
 *  buildPrettyString{
 *      space(4) //Not doing anything, just spacing
 *      space(4){ //Spacing with an action, makes organizing code easier
 *          append("This should be spaced by 8)
 *      }
 *  }
 * ```
 *
 * We can also add padding with a certain kind of character, such as add lines between paragraphs
 * ```
 *  buildPrettyString{
 *      "=" padded 12 //============
 *      bold{
 *          append("Some Important Message!!")
 *      }
 *      "=" padded 12
 *  }
 *  //=============Some Important Message!!=============
 * ```
 */
@ExperimentalStdlibApi
class PrettyPrinter{
    private var indentationLevel = 0
    private val sb = StringBuilder()

    @ExperimentalStdlibApi
    private val styleStack = ArrayDeque<PrettyColors>()

    init {
        styleStack.addLast(PrettyColors.RESET)
    }

    fun append(char: Char){
        append(char.toString())
    }

    fun append(int: Int){
        append(int.toString())
    }

    fun append(string: String){
        if(indentationLevel > 0){
            if(string.contains("\n")) {
                val lines = string.split(Regex("(?<=\n)"))
                for(line in lines.withIndex()){
                    if(line.value.isNotBlank()) {
                        (1..this.indentationLevel).forEach {
                            this.sb.append("\t")
                        }
                    }
                    this.sb.append(line.value)
                }
            }else{
                (1..this.indentationLevel).forEach {
                    this.sb.append("\t")
                }
                sb.append(string)
            }
        }else{
            this.sb.append(string)
        }
    }

    @ExperimentalStdlibApi
    fun red(block: PrettyPrinter.()->Unit){
        this.append(PrettyColors.RED.ansi)
        styleStack.addLast(PrettyColors.RED)
        this.block()
        styleStack.removeLast()
        this.append(styleStack.last().ansi)
    }

    fun green(block: PrettyPrinter.()->Unit){
        this.append(PrettyColors.GREEN.ansi)
        styleStack.addLast(PrettyColors.GREEN)
        this.block()
        styleStack.removeLast()
        this.append(styleStack.last().ansi)
    }

    fun blue(block: PrettyPrinter.()->Unit){
        this.append(PrettyColors.BLUE.ansi)
        styleStack.addLast(PrettyColors.BLUE)
        this.block()
        styleStack.removeLast()
        this.append(styleStack.last().ansi)
    }

    fun yellow(block: PrettyPrinter.()->Unit){
        this.append(PrettyColors.YELLOW.ansi)
        styleStack.addLast(PrettyColors.YELLOW)
        this.block()
        styleStack.removeLast()
        this.append(styleStack.last().ansi)
    }

    fun white(block: PrettyPrinter.()->Unit){
        this.append(PrettyColors.WHITE.ansi)
        styleStack.addLast(PrettyColors.WHITE)
        this.block()
        styleStack.removeLast()
        this.append(styleStack.last().ansi)
    }

    fun black(block: PrettyPrinter.()->Unit){
        this.append(PrettyColors.BLACK.ansi)
        styleStack.addLast(PrettyColors.BLACK)
        this.block()
        styleStack.removeLast()
        this.append(styleStack.last().ansi)
    }

    fun bold(block: PrettyPrinter.()->Unit){
        this.append(PrettyColors.BOLD.ansi)
        styleStack.addLast(PrettyColors.BOLD)
        this.block()
        styleStack.removeLast()
        this.append(styleStack.last().ansi)
    }

    fun italics(block: PrettyPrinter.()->Unit){
        this.append(PrettyColors.ITALIC.ansi)
        styleStack.addLast(PrettyColors.ITALIC)
        this.block()
        styleStack.removeLast()
        this.append(styleStack.last().ansi)
    }

    fun underline(block: PrettyPrinter.()->Unit){
        this.append(PrettyColors.UNDERLINE.ansi)
        styleStack.addLast(PrettyColors.UNDERLINE)
        this.block()
        styleStack.removeLast()
        this.append(styleStack.last().ansi)
    }

    fun appendWithNewLine(string: String){
        this.append("$string\n")
    }

    fun indent(block: PrettyPrinter.() -> Unit){
        this.indentationLevel++
        this.block()
        this.indentationLevel--
    }

    fun indentN(n: Int, block: PrettyPrinter.()->Unit){
        indentationLevel+=n
        block()
        indentationLevel-=n
    }

    fun spaced(n: Int, block: PrettyPrinter.()->Unit){
        spaced(n)
        block()
    }

    fun spaced(n: Int){
        for(i in 0..n){
            append(" ")
        }
    }

    infix fun Char.padded(n: Int){
        this padded 0..n
    }

    infix fun Char.padded(range: IntRange){
        val padding = buildPrettyString {
            for(i in range){
                append(this@padded)
            }
        }
        append(padding)
    }

    override fun toString(): String = this.sb.toString()

}

@ExperimentalStdlibApi
fun buildPrettyString(block: PrettyPrinter.()->Unit): String{
    val prettyPrinter = PrettyPrinter()
    prettyPrinter.block()
    return prettyPrinter.toString()
}