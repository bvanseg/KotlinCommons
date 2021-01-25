package bvanseg.kotlincommons.util.fp.monads

import bvanseg.kotlincommons.util.fp.monad.Either
import bvanseg.kotlincommons.util.fp.monad.flatMap
import bvanseg.kotlincommons.util.fp.monad.left
import bvanseg.kotlincommons.util.fp.monad.right
import org.junit.jupiter.api.Test

class EitherTest {
    @Test
    fun eitherLeftTest() {
        //Given
        val noInt = 5.left()
        //When
        //Then
        assert(noInt.isLeft)
    }

    @Test
    fun eitherRightTest() {
        //Given
        val noInt = 5.right()
        //When
        //Then
        assert(noInt.isRight)
    }

    @Test
    fun eitherMapTest() {
        //Given
        val five = 5.left()
        //When
        val fifteen = five.mapLeft { it + 10 }
        //Then
        assert(fifteen.isLeft)
        if (fifteen is Either.Left) {
            assert(fifteen.value == 15)
        }
    }

    @Test
    fun eitherFoldTest() {
        //Given
        val string = "Hello".left()
        val five = 10.right()
        //When
        val flat = string.fold({ "$it, world!" }, { ", everybody!" })
        //Then
        assert(flat.isLeft)
        if (flat is Either.Left) {
            assert(flat.value == "Hello, world!")
        }
    }

    @Test
    fun eitherMapRight() {
        //Given
        val five = 5.right()
        //When
        val map = five.mapRight { it + 10 }
        //Then
        assert(map.isRight)
        if (map is Either.Right) {
            assert(map.value == 15)
        }
    }

    private fun doSomeCalculation(i: Int): Either<Int, String> =
        when {
            i <= 10 -> (i * 10).left()
            i in 50..100 -> (i % 10).left()
            else -> "Could not do calculations on $i".right()
        }

    @Test
    fun eitherFlatMap() {
        //Given
        val calcResultTen = doSomeCalculation(10)
        val calcResult85 = doSomeCalculation(85)
        val calcResult22 = doSomeCalculation(22)
        //When
        val finalResult1 = calcResultTen.flatMap {
            (it % 2).left()
        }.mapRight {
            "An error occurred while trying to calculate with '10': $it"
        }
        val finalResult2 = calcResult85.flatMap {
            (it % 2).left()
        }.mapRight {
            "An error occurred while trying to calculate with '85': $it"
        }
        val finalResult3 = calcResult22.flatMap {
            (it % 2).left()
        }.mapRight {
            "An error occurred while trying to calculate with '22': $it"
        }
        //Then
        assert(finalResult1.isLeft)
        assert(finalResult2.isLeft)
        assert(finalResult3.isRight)
    }
}