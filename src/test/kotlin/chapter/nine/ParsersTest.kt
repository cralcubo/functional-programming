package chapter.nine

import chapter.four.Left
import chapter.four.Right
import chapter.nine.Parsers.Companion.Error
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ParsersTest {
    private val p = ParsersImpl()

    @Test
    fun parseChar() {
        p.run(p.char('c'), 'c'.toString()) shouldBe Right('c')
    }
    @Test
    fun parseCharFail() {
        p.run(p.char('d'), 'c'.toString()) shouldBe Left(Error)
    }

    @Test
    fun parseString() {
        p.run(p.string("casa"), "casa") shouldBe Right("casa")
    }

    @Test
    fun parseStringFail() {
        p.run(p.string("casas"), "casa") shouldBe Left(Error)
    }

    @Test
    fun runOr() {
        p.run(p.or(p.string("abra"), p.string("cadabra")), "abra") shouldBe Right("abra")

        p.run(p.or(p.string("abra"), p.string("cadabra")), "cadabra") shouldBe Right("cadabra")
    }

}