package chapter.eight

import chapter.eight.Prop.Companion.forAll
import chapter.six.SimpleRNG
import chapter.three.exists
import chapter.three.max
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PropTest {

    @Test
    fun test(){
        val smallInt = choose(-10, 10)

        val maxProp = forAll(smallInt.nonEmptyList()) { ns ->
            val mx = ns.max()
            !ns.exists { it > mx }
        }

        Prop.run(maxProp)
    }
}