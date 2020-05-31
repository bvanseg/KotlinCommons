package bvanseg.kotlincommons.armada

import bvanseg.kotlincommons.any.getLogger
import bvanseg.kotlincommons.armada.annotations.Command
import bvanseg.kotlincommons.armada.annotations.IntRange
import bvanseg.kotlincommons.armada.commands.CommandModule
import bvanseg.kotlincommons.armada.commands.InternalCommand
import bvanseg.kotlincommons.armada.contexts.EmptyContext
import bvanseg.kotlincommons.armada.gears.Gear
import bvanseg.kotlincommons.armada.utilities.Argument
import bvanseg.kotlincommons.armada.utilities.Union
import bvanseg.kotlincommons.logging.debug
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mockito
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import java.util.stream.Stream
import kotlin.reflect.KFunction

class InternalCommandTest {
	companion object {
		@JvmStatic
		@Suppress("unused")
		private fun intBounded() = Stream.of(
			arguments(0, 0),
			arguments(50, 50),
			arguments(100, 100),
			arguments(-1, 0),
			arguments(101, 100),
			arguments(5000, 100)
		)
	}

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

	@AfterEach
	fun after() {
		Mockito.validateMockitoUsage()
	}

	fun receive() {
		commandManager = CommandManager()
		gear = TestGear()
		prefix = "!"
		commandManager.addGear(gear)
		commandManager.addCommand(TestCommand())
		val scn = Scanner(System.`in`)
		while (true) {
			val input = scn.nextLine()
			try {
				if (input.startsWith(commandManager.prefix)) this.getLogger().debug(commandManager.execute(input,
					EmptyContext
				))
			} catch (e: Exception) {
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
		assertAll(
			{ assertEquals("testCommand", command.name) },
			{ assertEquals("Description", command.description) },
			{ assertEquals("!testCommand (text) (num)", command.usage("!")) }
		)
	}

	@Test
	fun createOptional() {
		// Given
		// When
		val command = createCommand(TestGear::testCommandOptional)

		// Then
		assertAll(
			{ assertEquals("testCommandOptional", command.name) },
			{ assertEquals("Description", command.description) },
			{ assertEquals("!testCommandOptional <text>", command.usage("!")) }
		)
	}

	@Test
	fun createWithUsage() {
		// Given
		// When
		val command = createCommand(TestGear::testCommandUsage)

		// Then
		assertAll(
			{ assertEquals("testCommandUsage", command.name) },
			{ assertEquals("Description", command.description) },
			{ assertEquals("!testCommandUsage usage1\n!testCommandUsage usage2", command.usage(null)) }
		)
	}

	@Test
	fun createWithExamples() {
		// Given
		// When
		val command = createCommand(TestGear::testCommandExamples)

		// Then
		assertAll(
			{ assertEquals("testCommandExamples", command.name) },
			{ assertEquals("!testCommandExamples example1\n!testCommandExamples example2", command.examples(null)) }
		)
	}

	@ParameterizedTest
	@MethodSource
	fun intBounded(num: Int, expected: Int) {
		// Given
		val command = spyCommand(TestGear::intBounded)

		// When
		command.invoke(num.toString(), EmptyContext)

		// Then
		argumentCaptor<Map<String, Any?>> {
			verify(command).callNamed(capture(), anyOrNull(), anyOrNull())

			assertAll(
				{ assertEquals(1, firstValue.size) },
				{ assertNotNull(firstValue["num"]) },
				{ assertEquals(expected, firstValue["num"]) }
			)
		}
	}

	@Test
	fun overflowString() {
		// Given
		val command = spyCommand(TestGear::overflowString)

		// When
		command.invoke("This is a few args", EmptyContext)

		// Then
		argumentCaptor<Map<String, Any?>> {
			verify(command).callNamed(capture(), anyOrNull(), anyOrNull())

			assertAll(
				{ assertEquals(1, firstValue.size) },
				{ assertNotNull(firstValue["overflow"]) },
				{ assertEquals("This is a few args", firstValue["overflow"]) }
			)
		}
	}

	@Test
	fun overflowArray() {
		// Given
		val command = spyCommand(TestGear::overflowArray)

		// When
		command.invoke("This is a few args", EmptyContext)

		// Then
		argumentCaptor<Map<String, Any?>> {
			verify(command).callNamed(capture(), anyOrNull(), anyOrNull())

			assertAll(
				{ assertEquals(1, firstValue.size) },
				{ assertNotNull(firstValue["overflow"]) },
				{ assertTrue(arrayOf("This", "is", "a", "few", "args").contentEquals(firstValue["overflow"] as Array<*>)) }
			)
		}
	}

	@Test
	fun overflowList() {
		// Given
		val command = spyCommand(TestGear::overflowList)

		// When
		command.invoke("This is a few args", EmptyContext)

		// Then
		argumentCaptor<Map<String, Any?>> {
			verify(command).callNamed(capture(), anyOrNull(), anyOrNull())

			assertAll(
				{ assertEquals(1, firstValue.size) },
				{ assertNotNull(firstValue["overflow"]) },
				{ assertEquals(listOf("This", "is", "a", "few", "args"), firstValue["overflow"]) }
			)
		}
	}

	@Test
	fun optionalsSingleParam() {
		// Given
		val command = spyCommand(TestGear::opt)

		// When
		command.invoke("1", EmptyContext)

		// Then
		argumentCaptor<Map<String, Any?>> {
			verify(command).callNamed(capture(), anyOrNull(), anyOrNull())

			assertAll(
				{ assertEquals(1L, firstValue["bar"]) }
			)
		}
	}

	@Suppress("UNCHECKED_CAST")
	private fun createCommand(function: KFunction<*>): InternalCommand =
		InternalCommand(commandManager as CommandManager<Any>, CommandModule(function.name, commandManager), function, gear)

	private fun spyCommand(function: KFunction<*>): InternalCommand = Mockito.spy(createCommand(function))

	class TestGear : Gear() {
		@Command("Description")
		fun testCommand(text: String, num: Int) = Unit

		@Command("Description")
		fun testCommandOptional(text: String = "") = Unit

		@Command
		fun test(text: String = "testme"): String = text

		@Command("Description", aliases = ["test"], usage = ["<PREFIX><NAME> usage1", "<PREFIX><NAME> usage2"])
		fun testCommandUsage(text: String) = Unit

		@Command(examples = ["<PREFIX><NAME> example1", "<PREFIX><NAME> example2"])
		fun testCommandExamples() = Unit

		@Command
		fun intBounded(@IntRange(0, 100) num: Int) = Unit

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

		@Command
		fun overflowString(overflow: String) = Unit

		@Command
		fun overflowArray(overflow: Array<String>) = Unit

		@Command
		fun overflowList(overflow: List<String>) = Unit

		@Command
		fun opt(bar: Long = 0L) = Unit
	}
}