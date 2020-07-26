package bvanseg.kotlincommons.fp.monads

import bvanseg.kotlincommons.fp.dataclasses.Option
import bvanseg.kotlincommons.fp.dataclasses.none
import bvanseg.kotlincommons.fp.dataclasses.some
import org.junit.jupiter.api.Test

class OptionTest{
    @Test
    fun optionNoneTest(){
        //Given
        val noInt = none<Int>()
        //When
        //Then
        assert(noInt.isNone)
    }

    @Test
    fun optionSomeTest(){
        //Given
        val noInt = 5.some()
        //When
        //Then
        assert(noInt.isSome)
    }

    @Test
    fun optionMapTest(){
        //Given
        val noInt = none<Int>()
        //When
        val five = noInt.map { 5 }
        //Then
        assert(five.isSome)
    }

    @Test
    fun optionFlatMapTest(){
        //Given
        val noInt = none<Int>()
        val five = 5.some()
        //When
        val flat = noInt.flatMap { five }
        //Then
        assert(flat.isSome)
        if(flat is Option.Some){
            assert(flat.t.isSome)
        }
    }

    @Test
    fun optionUnwrapTest(){
        //Given
        val five = 5.some()
        //When
        //Then
        assert(five.unwrap == 5)
    }

    @Test
    fun optionUnwrapOrElseTest(){
        //Given
        val noInt = none<Int>()
        //When
        val five = noInt.unwrapOrElse { 5 }
        //Then
        assert(five == 5)
    }
}

