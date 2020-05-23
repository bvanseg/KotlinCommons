package bvanseg.kotlincommons.armada

import bvanseg.kotlincommons.armada.annotations.IntRange
import bvanseg.kotlincommons.armada.annotations.Command
import bvanseg.kotlincommons.armada.commands.InternalCommand
import bvanseg.kotlincommons.armada.contexts.EmptyContext
import bvanseg.kotlincommons.armada.gears.Gear
import bvanseg.kotlincommons.armada.utilities.Argument
import bvanseg.kotlincommons.armada.utilities.Union
import bvanseg.kotlincommons.any.getLogger
import bvanseg.kotlincommons.armada.commands.CommandModule
import bvanseg.kotlincommons.logging.debug
import bvanseg.kotlincommons.logging.info
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import kotlin.reflect.KFunction

class InternalCommandTest {

    lateinit var commandManager: CommandManager<Long>
    lateinit var gear: Gear
    lateinit var prefix: String

    @BeforeEach
    fun setup() {
        commandManager = CommandManager()
        gear = TestGear()
        prefix = "!"
        commandManager.prefixes[0] = "@"
        commandManager.addGear(gear)
    }

    fun receive() {
        commandManager = CommandManager()
        gear = TestGear()
        prefix = "!"
        commandManager.addGear(gear)
        commandManager.addCommand(TestCommand())
        val scn = Scanner(System.`in`)
        while(true) {
            val input = scn.nextLine()
            try {
                if (input.startsWith(commandManager.prefix)) this.getLogger().debug(commandManager.execute(input,
                    EmptyContext
                ))
            }catch(e: Exception) {
                e.printStackTrace()
                continue
            }
        }
    }

    @Test
    fun create() {
        // Given
        // When
        val command = createCommand(TestGear::testCommand)

        // Then
        assertEquals("testCommand", command.name)
        assertEquals("Description", command.description)
        assertEquals("!testCommand (text) (num)", command.usage)
    }

    @Test
    fun createOptional() {
        // Given
        // When
        val command = createCommand(TestGear::testCommandOptional)

        // Then
        assertEquals("testCommandOptional", command.name)
        assertEquals("Description", command.description)
        assertEquals("!testCommandOptional <text>", command.usage)
    }

    @Test
    fun createWithUsage() {
        // Given
        // When
        val command = createCommand(TestGear::testCommandUsage)

        // Then
        assertEquals("testCommandUsage", command.name)
        assertEquals("Description", command.description)
    }

    @Suppress("UNCHECKED_CAST")
    private fun createCommand(function: KFunction<*>): InternalCommand =
        InternalCommand(commandManager as CommandManager<Any>, CommandModule(function.name, commandManager), function, gear)

    class TestGear : Gear() {

        var count = 0

        @Command("Description", false)
        fun testCommand(text: String, num: Int) = Unit

        @Command("Description", false)
        fun testCommandOptional(text: String = "") = Unit

        @Command
        fun test(text: String ="testme"): String = text

        @Command("Description", false, ["test"], ["usage1", "usage2"])
        fun testCommandUsage(text: String) = Unit

        @Command
        fun count() = count++

        @Command
        fun printNumber(num: Int) = this.getLogger().debug(num)

        @Command
        fun printNumberBounded(@IntRange(0, 100) num: Int) = this.getLogger().info(num)

        @Command
        fun union(data: Union<Int, Float>) {
            this.getLogger().debug(data.first)
            this.getLogger().debug(data.second)
            println(data.first)
            println(data.second)
        }

        @Command
        fun unionTrans(data: Union<BigInteger, BigDecimal>) {
            this.getLogger().debug(data.first)
            this.getLogger().debug(data.second)
            println(data.first)
            println(data.second)
        }

        @Command
        fun argTest(arg: Argument) {
            this.getLogger().debug(arg.getAsByte())
            this.getLogger().debug(arg.getAsShort())
            this.getLogger().debug(arg.getAsInt())
            this.getLogger().debug(arg.getAsLong())
            this.getLogger().debug(arg.getAsFloat())
            this.getLogger().debug(arg.getAsDouble())
            this.getLogger().debug(arg.getRaw())
            this.getLogger().debug(arg.transformTo(this.commandManager, Int::class))
        }

        @Command
        fun gearName() = this.getLogger().debug(this.name)

        @Command
        fun bInteger(b: BigInteger) = this.getLogger().debug(b)

        @Command
        fun bDecimal(b: BigDecimal) = this.getLogger().debug(b)

    }
}