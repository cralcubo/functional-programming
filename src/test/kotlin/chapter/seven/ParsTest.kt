package chapter.seven

import chapter.seven.Pars.fork
import chapter.seven.Pars.map
import chapter.seven.Pars.unit
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors

class ParsTest {

    @Test
    fun parMap() {
        val executor = Executors.newFixedThreadPool(5)
        val ls = listOf(1,2,3,4,5,6,7,8,9,10)
        val parMap = Pars.parMap(ls) { it * 10 }
        val future = parMap(executor)
        future.get() shouldBe listOf(10,20,30,40,50,60,70,80,90,100)
    }

    @Test
    fun parFilter() {
        val executor = Executors.newFixedThreadPool(50)
        val ls = listOf(1,2,3,4,5,6,7,8,9,10)
        val parMap = Pars.parFilter(ls) { it%2 == 0 }
        val future = parMap(executor)
        future.get() shouldBe listOf(2,4,6,8,10)
    }

    @Test
    fun mapUnit() {
        map(unit(1)) { it + 1 } shouldBe unit(2)
    }

    @Test
    fun deadLock() {
        val es = Executors.newFixedThreadPool(5) // it will deadlock if nrThreads < nr-fork{}
        val future = fork { fork { fork { unit(42) } } }(es)
        future.get() shouldBe 42
    }


}