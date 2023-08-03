package chapter.six

import chapter.four.getOrElse
import chapter.three.first
import chapter.three.List
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test

class SimpleRNGTest {

    @Test
    fun simple() {
        val s = SimpleRNG(Int.MAX_VALUE.toLong())
        s.nextInt().first shouldBe s.nextInt().first
        s.nextInt().second shouldBe s.nextInt().second
    }

    @Test
    fun newInt() {
        val s = SimpleRNG(10)
        s.nextInt() shouldNotBe s.nextInt().second.nextInt()

        val pair = intR.run(s)
        s.nextInt().first shouldBe pair.first
        s.nextInt().second shouldBe pair.second
    }



    @Test
    fun list() {
        val (ints, rng) = ints(1, SimpleRNG(1))
        ints.first().getOrElse { 0 } shouldBe SimpleRNG(1).nextInt().first
        rng shouldBe SimpleRNG(1).nextInt().second
    }

    @Test
    fun sequence() {
        val ls = List.of(intR, intR)
        val sequence = sequence(ls).run(SimpleRNG(1))
        println(sequence.first)
    }

    @Test
    fun doubleR() {
        val s = SimpleRNG(10)
        val intDoubleR: Rand<Pair<Int, Double>> = both(intR, doubleR)
        intDouble(s).second shouldBe intDoubleR.run(s).second
    }




}