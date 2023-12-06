package chapter.ten

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class WordCounterTest {

    @Test
    fun wcMonoidLaws() {
        val wcMonoid = wcMonoid()
        // "lorem ipsum do|lor sit amet, "
        wcMonoid.combine(Part("lorem", 1, "do"), wcMonoid.nil) shouldBe Part("lorem", 1, "do")
        wcMonoid.combine(wcMonoid.nil, Part("lor", 2, "")) shouldBe Part("lor", 2, "")
        wcMonoid.combine(Part("lorem", 1, "do"), Part("lor", 2, "")) shouldBe Part("lorem", 4, "")
    }

    @Test
    fun wordsCounter() {
        // 69 words
        val s = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
        wordCount(s) shouldBe 69
    }
}