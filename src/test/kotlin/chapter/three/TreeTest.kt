package chapter.three

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TreeTest {
    @Test
    fun map() {
        val t = Branch(Branch(Leaf(1), Leaf(2)), Branch(Leaf(3), Leaf(4)))
        t.map { "$it" } shouldBe Branch(Branch(Leaf("1"), Leaf("2")), Branch(Leaf("3"), Leaf("4")))
    }
}