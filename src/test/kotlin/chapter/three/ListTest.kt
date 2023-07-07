package chapter.three

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ListTest {

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

}

