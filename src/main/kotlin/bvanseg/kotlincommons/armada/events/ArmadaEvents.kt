package bvanseg.kotlincommons.armada.events

import bvanseg.kotlincommons.armada.CommandManager
import bvanseg.kotlincommons.armada.commands.BaseCommand
import bvanseg.kotlincommons.armada.gears.Gear
import bvanseg.kotlincommons.armada.transformers.Transformer

class Init: ArmadaEvent()

open class CommandEvent(open val manager: CommandManager<*>): ArmadaEvent()
open class CommandExecuteEvent(override val manager: CommandManager<*>): CommandEvent(manager) {
    class Pre(override val manager: CommandManager<*>): CommandExecuteEvent(manager)
    class Post(override val manager: CommandManager<*>): CommandExecuteEvent(manager)
}
open class CommandAddEvent(open val command: BaseCommand, override val manager: CommandManager<*>): CommandEvent(manager) {
    class Pre(override val command: BaseCommand, override val manager: CommandManager<*>) : CommandAddEvent(command, manager)
    class Post(override val command: BaseCommand, override val manager: CommandManager<*>) : CommandAddEvent(command, manager)
}

open class GearEvent(open val gear: Gear, open val manager: CommandManager<*>): ArmadaEvent()
open class GearAddEvent(override val gear: Gear, override val manager: CommandManager<*>): GearEvent(gear, manager) {
    class Pre(override val gear: Gear, override val manager: CommandManager<*>) : GearAddEvent(gear, manager)
    class Post(override val gear: Gear, override val manager: CommandManager<*>) : GearAddEvent(gear, manager)
}

open class TransformerEvent(open val transformer: Transformer<*>, open val manager: CommandManager<*>): ArmadaEvent()
open class TransformerAddEvent(override val transformer: Transformer<*>, override val manager: CommandManager<*>): TransformerEvent(transformer, manager) {
    class Pre(override val transformer: Transformer<*>, override val manager: CommandManager<*>) : TransformerAddEvent(transformer, manager)
    class Post(override val transformer: Transformer<*>, override val manager: CommandManager<*>) : TransformerAddEvent(transformer, manager)
}