package chapter.three

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ListTest {

    @Test
    fun drop() {
        val ls = List.of(1,2,3,4)
        List.drop(ls, 2) shouldBe List.of(3,4)
    }

    @Test
    fun init() {
        val ls = List.of(1,2,3,4)
        List.init(ls) shouldBe List.of(1,2,3)
    }

    @Test
    fun length() {
        List.length(List.of(1,2,3,56)) shouldBe 4
    }
}