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
package bvanseg.kotlincommons.command.event

import bvanseg.kotlincommons.command.BaseCommand
import bvanseg.kotlincommons.command.CommandManager
import bvanseg.kotlincommons.command.InternalCommand
import bvanseg.kotlincommons.command.context.Context
import bvanseg.kotlincommons.command.gear.Gear
import bvanseg.kotlincommons.command.transformer.Transformer
import bvanseg.kotlincommons.command.validation.Validator
import kotlin.reflect.KClass

class CommandManagerInitializationEvent : BaseCommandEvent()

open class CommandEvent(val manager: CommandManager<*>) : BaseCommandEvent()
open class CommandExecuteEvent(manager: CommandManager<*>, val command: InternalCommand, val context: Context) :
    CommandEvent(manager) {
    class Pre(manager: CommandManager<*>, command: InternalCommand, context: Context) :
        CommandExecuteEvent(manager, command, context)

    class Post(manager: CommandManager<*>, command: InternalCommand, context: Context, val result: Any?) :
        CommandExecuteEvent(manager, command, context)
}

open class CommandAddEvent(val command: BaseCommand, manager: CommandManager<*>) : CommandEvent(manager) {
    class Pre(command: BaseCommand, manager: CommandManager<*>) : CommandAddEvent(command, manager)
    class Post(command: BaseCommand, manager: CommandManager<*>) : CommandAddEvent(command, manager)
}

open class GearEvent(val gear: Gear, val manager: CommandManager<*>) : BaseCommandEvent()
open class GearAddEvent(gear: Gear, manager: CommandManager<*>) : GearEvent(gear, manager) {
    class Pre(gear: Gear, manager: CommandManager<*>) : GearAddEvent(gear, manager)
    class Post(gear: Gear, manager: CommandManager<*>) : GearAddEvent(gear, manager)
}

open class TransformerEvent(val transformer: Transformer<*>, val manager: CommandManager<*>) : BaseCommandEvent()
open class TransformerAddEvent(transformer: Transformer<*>, manager: CommandManager<*>) :
    TransformerEvent(transformer, manager) {
    class Pre(transformer: Transformer<*>, manager: CommandManager<*>) : TransformerAddEvent(transformer, manager)
    class Post(transformer: Transformer<*>, manager: CommandManager<*>) : TransformerAddEvent(transformer, manager)
}

open class ValidatorEvent(val validators: List<Validator<*, *>>, val manager: CommandManager<*>) : BaseCommandEvent()
open class ValidatorAddEvent(
    val annotationClass: KClass<out Annotation>,
    validators: List<Validator<*, *>>,
    manager: CommandManager<*>
) :
    ValidatorEvent(validators, manager) {
    class Pre(annotationClass: KClass<out Annotation>, validators: List<Validator<*, *>>, manager: CommandManager<*>) :
        ValidatorAddEvent(annotationClass, validators, manager)

    class Post(annotationClass: KClass<out Annotation>, validators: List<Validator<*, *>>, manager: CommandManager<*>) :
        ValidatorAddEvent(annotationClass, validators, manager)
}
