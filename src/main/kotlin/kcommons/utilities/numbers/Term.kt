package kcommons.utilities.numbers

class Term(var term: String) {

    var operation: Op = Op.NULL
    var level = 0
    var otherTerm: Term? = null
}