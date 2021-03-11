package bvanseg.kotlincommons.lang.command.exception

sealed class CommandException(message: String? = null): Exception(message)

// Arguments
class DuplicateArgumentTypeException(message: String? = null): CommandException(message)
class IllegalTokenTypeException(message: String? = null): CommandException(message)
class MissingArgumentException(message: String? = null): CommandException(message)

// Executors
class DuplicateExecutorException(message: String? = null): CommandException(message)
class MissingExecutorException(message: String? = null): CommandException(message)

// Literals
class DuplicateLiteralException(message: String? = null): CommandException(message)