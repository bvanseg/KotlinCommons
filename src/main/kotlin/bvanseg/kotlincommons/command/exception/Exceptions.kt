/*
 * MIT License
 *
 * Copyright (c) 2021 Boston Vanseghi
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
package bvanseg.kotlincommons.command.exception

import bvanseg.kotlincommons.command.context.Context

/**
 * The most basic [Exception] that command-related code will throw.
 *
 * @author Boston Vanseghi
 * @since 2.2.4
 */
open class CommandException(override val message: String) : Exception(message)

/**
 * Thrown when a generic exception occurs regarding parameters.
 *
 * @author Boston Vanseghi
 * @since 2.2.4
 */
open class ParameterException(override val message: String) : CommandException(message)

/**
 * Thrown when a generic exception occurs regarding transformers.
 *
 * @author Boston Vanseghi
 * @since 2.2.4
 */
open class TransformerException(override val message: String) : CommandException(message)

/**
 * Thrown when a generic exception occurs regarding validators.
 *
 * @author Boston Vanseghi
 * @since 2.7.0
 */
open class ValidatorException(override val message: String) : CommandException(message)

/**
 * Thrown when a parameter is missing during command execution.
 *
 * @author Boston Vanseghi
 * @since 2.2.4
 */
class MissingParameterException(val parameters: List<String>, override val message: String) :
    ParameterException(message)

/**
 * Thrown when a given parameter is invalid (such as a [String] passed in for an [Int]).
 *
 * @author Boston Vanseghi
 * @since 2.2.4
 */
class InvalidParameterException(override val message: String) : ParameterException(message)

/**
 * Thrown when a given argument can't be matched up to any parameter.
 *
 * @author Boston Vanseghi
 * @since 2.2.4
 */
class UnknownParameterException(val ctx: Context, override val message: String) : ParameterException(message)

/**
 * Thrown when a [bvanseg.kotlincommons.command.transformer.Transformer] can't be found for parsing a command argument.
 *
 * @author Boston Vanseghi
 * @since 2.2.4
 */
class MissingTransformerException(val ctx: Context, override val message: String) : TransformerException(message)

/**
 * Thrown when a [bvanseg.kotlincommons.command.transformer.Transformer] encounters an error during parsing.
 *
 * @author Boston Vanseghi
 * @since 2.2.4
 */
class TransformerParseException(val ctx: Context, override val message: String) : TransformerException(message)

/**
 * Thrown when a [bvanseg.kotlincommons.command.transformer.Transformer] with the same type is registered a second time.
 *
 * @author Boston Vanseghi
 * @since 2.2.4
 */
class DuplicateTransformerException(override val message: String) : TransformerException(message)

/**
 * Thrown when a [bvanseg.kotlincommons.command.validation.Validator] with the same type is registered a second time.
 *
 * @author Boston Vanseghi
 * @since 2.7.0
 */
class DuplicateValidatorException(override val message: String) : ValidatorException(message)

/**
 * Thrown when an unknown [CommandException] occurs.
 *
 * @author Boston Vanseghi
 * @since 2.2.4
 */
class UnknownCommandException(override val message: String) : CommandException(message)