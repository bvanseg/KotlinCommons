package bvanseg.kotlincommons.number

import bvanseg.kotlincommons.string.ToStringBuilder

class HashCodeBuilderTest {

    class Account {
        val balance = 0.0
        val foo = arrayOf("Bar", "FooBar", "MoreFooBar")

        override fun toString() = ToStringBuilder.builder(this::class)
            .append("balance", balance)
            .append("foo", foo)
            .toString()

        override fun hashCode(): Int = HashCodeBuilder(this)
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
        override fun toString(): String = ToStringBuilder.builder(this::class)
            .append("name", name)
            .append("age", age)
            .append("gender", gender)
            .append("account", acct)
            .toString()

        override fun hashCode(): Int = HashCodeBuilder(this)
            .append(name)
            .append(age)
            .append(gender)
            .append(acct)
            .hashCode()
    }
}