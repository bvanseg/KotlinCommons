package bvanseg.utilities.numbers

import bvanseg.utilities.string.UTFBuffer

class Expression(expression: String) {

    private var buffer = UTFBuffer.wrap(expression)

    val expressionMap = hashMapOf<Int, MutableList<Term>>()

    var flag = false

    fun parse() {
        var level = 0

        var lastTerm: Term? = null
        val curTerm = CharArray(1000)
        var pos = 0
        while(buffer.isNotEmpty()) {
            if(expressionMap[level] == null){
                expressionMap[level] = mutableListOf()
            }
            val curChar = buffer.nextChar()
            pos++

            when (curChar) {
                '+' -> {
                    val term = Term(curTerm.toString())
                    term.level = level
                    term.operation = Op.ADD
                    lastTerm?.let {
                        // TODO: Check if they're the same level
                        it.otherTerm = term
                    }

                    expressionMap[level]?.add(term)
                }
                '-' -> {}
                '*' -> {}
                '/' -> {}
                '%' -> {}
                '(' -> level-- // TODO: How can we handle 1 + (-1) + 1? The parser will think that middle term is a different level.
                ')' -> level++
                else -> curTerm[pos] = curChar
            }
        }


    }
}