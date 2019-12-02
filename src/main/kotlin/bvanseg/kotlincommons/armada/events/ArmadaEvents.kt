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
package bvanseg.kotlincommons.armada.events

import bvanseg.kotlincommons.armada.CommandManager
import bvanseg.kotlincommons.armada.commands.BaseCommand
import bvanseg.kotlincommons.armada.commands.InternalCommand
import bvanseg.kotlincommons.armada.gears.Gear
import bvanseg.kotlincommons.armada.transformers.Transformer

class Init : ArmadaEvent()

open class CommandEvent(open val manager: CommandManager<*>) : ArmadaEvent()
open class CommandExecuteEvent(override val manager: CommandManager<*>, val command: InternalCommand) :
    CommandEvent(manager) {
    class Pre(override val manager: CommandManager<*>, command: InternalCommand) : CommandExecuteEvent(manager, command)
    class Post(override val manager: CommandManager<*>, command: InternalCommand, val result: Any?) :
        CommandExecuteEvent(manager, command)
}

open class CommandAddEvent(open val command: BaseCommand, override val manager: CommandManager<*>) :
    CommandEvent(manager) {
    class Pre(override val command: BaseCommand, override val manager: CommandManager<*>) :
        CommandAddEvent(command, manager)

    class Post(override val command: BaseCommand, override val manager: CommandManager<*>) :
        CommandAddEvent(command, manager)
}

open class GearEvent(open val gear: Gear, open val manager: CommandManager<*>) : ArmadaEvent()
open class GearAddEvent(override val gear: Gear, override val manager: CommandManager<*>) : GearEvent(gear, manager) {
    class Pre(override val gear: Gear, override val manager: CommandManager<*>) : GearAddEvent(gear, manager)
    class Post(override val gear: Gear, override val manager: CommandManager<*>) : GearAddEvent(gear, manager)
}

open class TransformerEvent(open val transformer: Transformer<*>, open val manager: CommandManager<*>) : ArmadaEvent()
open class TransformerAddEvent(override val transformer: Transformer<*>, override val manager: CommandManager<*>) :
    TransformerEvent(transformer, manager) {
    class Pre(override val transformer: Transformer<*>, override val manager: CommandManager<*>) :
        TransformerAddEvent(transformer, manager)

    class Post(override val transformer: Transformer<*>, override val manager: CommandManager<*>) :
        TransformerAddEvent(transformer, manager)
}
