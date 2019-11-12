import bvanseg.kotlincommons.armada.annotations.Invoke
import bvanseg.kotlincommons.armada.commands.BaseCommand
import java.math.BigInteger

class TestCommand: BaseCommand() {

    init {
        this.gear = "test"
        this.description = "RADAAAAA RADA!"
    }

    @Invoke
    fun rada(bInt: BigInteger) {
        println(bInt)
    }

    @Invoke
    fun rada(f: Int, f2: Int) {
        println(f)
        println(f2)
    }
}