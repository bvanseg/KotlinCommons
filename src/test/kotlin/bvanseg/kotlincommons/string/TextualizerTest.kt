package bvanseg.kotlincommons.string

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * @author Boston Vanseghi
 */
internal class TextualizerTest {
    class Account {
        val balance = 0.0
    }

    enum class Gender {
        MALE,
        FEMALE,
        OTHER
    }

    class Person {
        val name = "Bob"
        val age = "22"
        val gender = Gender.MALE
        val acct: Account = Account()
        override fun toString(): String = Textualizer.builder()
            .append("name", name)
            .append("age", age)
            .append("gender", gender)
            .append("account", acct)
            .toString()
    }

    @Test
    fun textualize() {
        // Given
        val textualizer = Textualizer.builder()

        // When
        val person = Person()
        val data = person.toString()

        // Then
        println(data)
        assertEquals("[name=Bob, age=22, gender=MALE, account=${Account::class.qualifiedName}]", data)
    }
}