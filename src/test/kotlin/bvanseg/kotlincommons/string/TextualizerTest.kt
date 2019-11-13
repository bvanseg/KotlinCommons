package bvanseg.kotlincommons.string

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * @author Boston Vanseghi
 */
internal class TextualizerTest {
    class Account {
        val balance = 0.0
        val foo = arrayOf("Bar", "FooBar", "MoreFooBar")
        val bar = mapOf("boo" to 1, "ooga" to 2)

        override fun toString() = Textualizer.builder(this)
            .append("balance", balance)
            .append("foo", foo)
            .append("bar", bar)
            .toString()
    }

    enum class Gender {
        MALE,
        FEMALE,
        OTHER
    }

    class Person {
        val name = "Bob"
        val age = 22
        val gender = Gender.MALE
        val acct: Account = Account()
        override fun toString(): String = Textualizer.builder(this)
            .append("name", name)
            .append("age", age)
            .append("gender", gender)
            .append("account", acct)
            .toString()
    }

    @Test
    fun textualize() {
        // Given
        // When
        val person = Person()
        val data = person.toString()

        // Then
        println(data)
        assertEquals("Person(name=\"Bob\", age=22, gender=MALE, account=Account(balance=0.0, foo=[Bar, FooBar, MoreFooBar], bar={boo=1, ooga=2}))", data)
    }
}