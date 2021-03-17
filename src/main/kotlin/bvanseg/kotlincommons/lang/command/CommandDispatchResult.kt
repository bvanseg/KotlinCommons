package bvanseg.kotlincommons.lang.command

/**
 * @author Boston Vanseghi
 * @since 2.10.4
 */
enum class CommandDispatchResult {
    COMMAND_FIRING_CANCELLED,
    INVALID_INPUT,
    UNKNOWN_COMMAND
}