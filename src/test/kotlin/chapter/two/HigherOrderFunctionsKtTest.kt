package chapter.two

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class HigherOrderFunctionsKtTest {

    @Test
    fun isSorted() {
        val ls = listOf(1,2,3)
        isSortedBook(ls) { a, b -> a < b } shouldBe true
    }

    @Test
    fun isNotSorted() {
        val ls = listOf(1,2,3,0)
        isSortedBook(ls) { a, b -> a < b } shouldBe false
    }
}