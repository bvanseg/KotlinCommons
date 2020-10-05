package bvanseg.kotlincommons.fp.monads

import bvanseg.kotlincommons.fp.dataclass.Option
import bvanseg.kotlincommons.fp.dataclass.none
import bvanseg.kotlincommons.fp.dataclass.some
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
        val five = 5.some()
        //When
        val mapNone = noInt.map { 5 }
        val mapFive = five.map { 10 }
        //Then
        assert(mapNone.isNone)
        assert(mapFive.isSome)
    }

    @Test
    fun optionFlatMapTest(){
        //Given
        val noInt = none<Int>()
        val five = 5.some()
        //When
        val flatNone = noInt.flatMap { five }
        val flatFive = five.flatMap { 10.some() }
        //Then
        assert(flatNone.isNone)
        assert(flatFive.isSome)
        if(flatFive is Option.Some){
            assert(flatFive.t == 10)
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

