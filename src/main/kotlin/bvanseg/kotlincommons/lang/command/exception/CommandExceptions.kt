package bvanseg.kotlincommons.lang.command

sealed class CommandException(message: String? = null): Exception(message)

class DuplicateArgumentTypeException(message: String? = null): CommandException(message)
class DuplicateExecutorException(message: String? = null): CommandException(message)
class DuplicateLiteralException(message: String? = null): CommandException(message)