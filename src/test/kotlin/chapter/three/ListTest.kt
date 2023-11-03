package chapter.three

import chapter.four.getOrElse
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ListTest {

    @Test
    fun existsCond() {
        val ls = List.of(1,2,3,4)
        ls.exists { it > 10 } shouldBe false
        ls.exists { it < 10 } shouldBe true
    }

    @Test
    fun exists() {
        val ls = List.of(1,2,3,4)
        ls.exists(4) shouldBe true
        ls.exists(10) shouldBe false
    }

    @Test
    fun max() {
        val ls = List.of(-1,-200,-3,-100)
        ls.max() shouldBe -1
    }

    @Test
    fun genOne() {
        List.generateList(3, 1) { it } shouldBe List.of(1,1,1)
    }

    @Test
    fun genSec() {
        List.generateList(3, 0) { it + 1 } shouldBe List.of(0, 1, 2)
    }

    @Test
    fun fib() {
        // 0 1 1 2 3 5 8 13
        List.generateList(15, Pair(0, 1)) { Pair(it.second + it.first, it.first + it.second + it.second) }
            .flatMap { List.of(it.first, it.second) }.last().getOrElse { 0 } shouldBe 514229
    }

    @Test
    fun size() {
        List.of(1,2,3,4,5).size() shouldBe 5
    }


    @Test
    fun drop() {
        val ls = List.of(1, 2, 3, 4)
        List.drop(ls, 2) shouldBe List.of(3, 4)
    }

    @Test
    fun init() {
        val ls = List.of(1, 2, 3, 4)
        List.init(ls) shouldBe List.of(1, 2, 3)
    }

    @Test
    fun length() {
        List.length(List.of(1, 2, 3, 56)) shouldBe 4
    }

    @Test
    fun revert() {
        List.revert(List.of(1, 2, 3)).shouldBe(List.of(3, 2, 1))
    }

    @Test
    fun concat() {
        List.concat(List.of(List.of(1, 2), Nil, List.of(3, 4))) shouldBe List.of(1, 2, 3, 4)
    }

    @Test
    fun filter() {
        val ls = List.of(1, 2, 3, 4)
        List.filter(ls) { a -> a % 2 == 0 } shouldBe List.of(2, 4)
    }

    @Test
    fun intAdd() {
        val lsa = List.of(1, 2, 3, 4)
        val lsb = List.of(1, 2, 3)
        List.intAdd(lsa, lsb) shouldBe List.of(2,4,6)
    }

    @Test
    fun first() {
        val ls = List.of(1, 2, 3, 4)
        ls.first().getOrElse { 0 } shouldBe 1
        Nil.first().getOrElse { 0 } shouldBe 0
    }

    @Test
    fun last() {
        val ls = List.of(1, 2, 3, 4)
        ls.last().getOrElse { 0 } shouldBe 4
        Nil.last().getOrElse { 0 } shouldBe 0

    }

}
