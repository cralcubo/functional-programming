package chapter.seven

import chapter.seven.OldPar.Companion.fork
import chapter.six.unit
import org.junit.jupiter.api.Test

class ParTest {

    @Test
    fun fork(){
        val par = OldPar(5)
        val fork = fork {
            println("Fork executed")
            par
        }

//        fork shouldBe par
    }

    @Test
    fun unit() {
        val p = unit {
            println("Instantiated!!!")
            42
        }
    }

}

