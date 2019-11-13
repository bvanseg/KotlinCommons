package bvanseg.kotlincommons.numbers

import bvanseg.kotlincommons.string.Textualizer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HasherTest {

    class Account {
        val balance = 0.0
        val foo = arrayOf("Bar", "FooBar", "MoreFooBar")

        override fun toString() = Textualizer.builder(this)
            .append("balance", balance)
            .append("foo", foo)
            .toString()

        override fun hashCode(): Int = Hasher.builder(this)
            .append(balance)
            .append(foo)
            .hashCode()
    }

    enum class Gender {
        MALE,
        FEMALE,
        OTHER
    }

    class Person {
        val name = "Bob"
        var age = 22
        val gender = Gender.MALE
        val acct: Account = Account()
        override fun toString(): String = Textualizer.builder(this)
            .append("name", name)
            .append("age", age)
            .append("gender", gender)
            .append("account", acct)
            .toString()

        override fun hashCode(): Int = Hasher.builder(this)
            .append(name)
            .append(age)
            .append(gender)
            .append(acct)
            .hashCode()
    }

    @Test
    fun testHasher() {
        // Given
        // When
//        val person = Person()
//        println(person.hashCode())
//        person.age++
//        println(person.hashCode())
    }
}