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
package bvanseg.kotlincommons.util.command.event

import bvanseg.kotlincommons.util.command.BaseCommand
import bvanseg.kotlincommons.util.command.CommandManager
import bvanseg.kotlincommons.util.command.InternalCommand
import bvanseg.kotlincommons.util.command.context.Context
import bvanseg.kotlincommons.util.command.gear.Gear
import bvanseg.kotlincommons.util.command.transformer.Transformer
import bvanseg.kotlincommons.util.command.validator.Validator
import kotlin.reflect.KClass

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
class CommandManagerInitializationEvent : BaseCommandEvent()

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
open class CommandEvent(val manager: CommandManager<*>) : BaseCommandEvent()
@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
open class CommandExecuteEvent(manager: CommandManager<*>, val command: InternalCommand, val context: Context) :
    CommandEvent(manager) {
    @Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
    class Pre(manager: CommandManager<*>, command: InternalCommand, context: Context) :
        CommandExecuteEvent(manager, command, context)

    @Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
    class Post(manager: CommandManager<*>, command: InternalCommand, context: Context, val result: Any?) :
        CommandExecuteEvent(manager, command, context)
}

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
open class CommandAddEvent(val command: BaseCommand, manager: CommandManager<*>) : CommandEvent(manager) {
    @Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
    class Pre(command: BaseCommand, manager: CommandManager<*>) : CommandAddEvent(command, manager)
    @Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
    class Post(command: BaseCommand, manager: CommandManager<*>) : CommandAddEvent(command, manager)
}

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
open class GearEvent(val gear: Gear, val manager: CommandManager<*>) : BaseCommandEvent()
@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
open class GearAddEvent(gear: Gear, manager: CommandManager<*>) : GearEvent(gear, manager) {
    @Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
    class Pre(gear: Gear, manager: CommandManager<*>) : GearAddEvent(gear, manager)
    @Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
    class Post(gear: Gear, manager: CommandManager<*>) : GearAddEvent(gear, manager)
}

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
open class TransformerEvent(val transformer: Transformer<*>, val manager: CommandManager<*>) : BaseCommandEvent()
@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
open class TransformerAddEvent(transformer: Transformer<*>, manager: CommandManager<*>) :
    TransformerEvent(transformer, manager) {
    @Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
    class Pre(transformer: Transformer<*>, manager: CommandManager<*>) : TransformerAddEvent(transformer, manager)
    @Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
    class Post(transformer: Transformer<*>, manager: CommandManager<*>) : TransformerAddEvent(transformer, manager)
}

@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
open class ValidatorEvent(val validators: List<Validator<*, *>>, val manager: CommandManager<*>) : BaseCommandEvent()
@Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
open class ValidatorAddEvent(
    val annotationClass: KClass<out Annotation>,
    validators: List<Validator<*, *>>,
    manager: CommandManager<*>
) :
    ValidatorEvent(validators, manager) {
    @Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
    class Pre(annotationClass: KClass<out Annotation>, validators: List<Validator<*, *>>, manager: CommandManager<*>) :
        ValidatorAddEvent(annotationClass, validators, manager)

    @Deprecated("Scheduled for removal in KotlinCommons 2.10.0")
    class Post(annotationClass: KClass<out Annotation>, validators: List<Validator<*, *>>, manager: CommandManager<*>) :
        ValidatorAddEvent(annotationClass, validators, manager)
}
