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
package bvanseg.kotlincommons.lang.command.exception

sealed class CommandException(message: String? = null) : Exception(message)

// Arguments
open class ArgumentException(message: String? = null): CommandException(message)
class DuplicateArgumentTypeException(message: String? = null) : ArgumentException(message)
class IllegalTokenTypeException(message: String? = null) : ArgumentException(message)
class MissingArgumentException(message: String? = null) : ArgumentException(message)

// Executors
sealed class ExecutorException(message: String? = null) : CommandException(message)
class DuplicateExecutorException(message: String? = null) : ExecutorException(message)
class MissingExecutorException(message: String? = null) : ExecutorException(message)

// Literals
sealed class LiteralException(message: String? = null) : CommandException(message)
class DuplicateLiteralException(message: String? = null) : LiteralException(message)

// Exception Catchers
sealed class ExceptionCatcherException(message: String? = null) : CommandException(message)
class DuplicateCatcherException(message: String? = null) : ExceptionCatcherException(message)

// Transformers
class TransformerException(message: String? = null) : CommandException(message)