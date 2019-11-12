package bvanseg.kotlincommons.armada.commands

abstract class BaseCommand {
    var gear: String = ""
    var description: String = ""
    var rawArgs: Boolean = false
    var aliases: List<String> = mutableListOf()
    var usage: List<String> = mutableListOf()
}