package chapter.ten

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class MonoidTest {

    @Test
    fun intAdditionLaws() {
        val additionMonoid = intAddition()
        additionMonoid.combine(2,additionMonoid.nil) shouldBe 2
        additionMonoid.combine(additionMonoid.nil, 3) shouldBe 3
        additionMonoid.combine(2,3) shouldBe 5
    }
}