package chapter.seven

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class FunctionalParallelismKtTest {

    @Test
    fun sumPar() {
        sum(listOf(1,2,3,4)).get shouldBe 10
    }
}