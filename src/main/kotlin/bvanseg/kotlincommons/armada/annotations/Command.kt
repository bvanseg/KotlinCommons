/*
 * MIT License
 *
 * Copyright (c) 2019 Boston Vanseghi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package bvanseg.kotlincommons.armada.annotations

/**
 * The basis for all commands. Any functions within a [bvanseg.kotlincommons.armada.gears.Gear] annotated with this
 * annotation will be designated as a command.
 *
 * @author Boston Vanseghi
 * @since 2.1.0
 */
@Target(AnnotationTarget.FUNCTION)
annotation class Command(
	/**
	 * A description of what the command does.
	 */
	val description: String = "",
	/**
	 * Whether command arguments should instead not be parsed into declared types, and instead just be passed directly
	 * as an [Array] of [String]s.
	 */
	val rawArgs: Boolean = false,
	/**
	 * An [Array] of other names that can be used to execute this command.
	 */
	val aliases: Array<String> = [],
	/**
	 * An [Array] of [String]s showing how to use this command.
	 * Any cases of the text "<PREFIX>" will be replaced with the appropriate command prefix.
	 * Any cases of the text "<NAME>" will be replaced with the command name.
	 *
	 * If no value is provided here, then a default usage will be generated.
	 */
	val usage: Array<String> = []
)