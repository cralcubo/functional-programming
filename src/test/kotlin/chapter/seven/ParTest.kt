package chapter.seven

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ParTest {

    @Test
    fun unit() {
        val p = unit {
            println("Instantiated!!!")
            42
        }
    }

    @Test
    fun unit2() {
        val p2 = unit2 {
            println("Instantiated!!!")
            42
        }

        println("I'm going to call get in Par")
        println(p2.get())
    }
}

