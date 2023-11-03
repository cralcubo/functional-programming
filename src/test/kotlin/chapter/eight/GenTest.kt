package chapter.eight

import chapter.eight.Gen.Companion.listOfN
import chapter.eight.Gen.Companion.listOfN2
import chapter.eight.Gen.Companion.sequence
import chapter.eight.Gen.Companion.union
import chapter.eight.Gen.Companion.unit
import chapter.six.SimpleRNG
import chapter.six.State
import chapter.three.List
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test

class GenTest {

    @Test
    fun union() {
        val ga = Gen.unit(5)
        val gb = Gen.unit(10)
        val gc = Gen.union(ga, gb)
        val gc2 = Gen.union(ga,gb)

        val pair = gc.sample.run(SimpleRNG(1))
        println(pair)
        val pair2 = gc2.sample.run(SimpleRNG(1))
        println(pair2)

    }

    @Test
    fun unit() {
        val unit = unit(5L)
        val state = SimpleRNG(1)
        val run = unit.sample.run(state)
        run.first shouldBe 5L
        run.second shouldBe state
    }

    @Test
    fun choose() {
        val gen = choose(5, 100)
        val state = SimpleRNG(1)
        val rngPair = gen.sample.run(state)

        (rngPair.first > 5) shouldBe true
        (rngPair.first < 100) shouldBe true
        rngPair.second shouldNotBe  state
    }

    @Test
    fun list() {
        val listGen = Gen.listOfN2(3, choose(0, 1000))
        val rng = SimpleRNG(10)
        val pair = listGen.sample.run(rng)
        println(pair)

        pair.first shouldBe List.of(489, 366, 10)
        pair.second shouldNotBe rng
    }

    @Test
    fun list2() {
        val listOfN2 = listOfN(unit(3), choose(0, 1000))
        val rng = SimpleRNG(10)
        val pair = listOfN2.sample.run(rng)
        println(pair)

        pair.first shouldBe List.of(489, 366, 10)
        pair.second shouldNotBe rng
    }

    @Test
    fun list3() {
        val listOfN2 = listOfN(unit(3), Gen(State.unit("casa")))
        val rng = SimpleRNG(10)
        val pair = listOfN2.sample.run(rng)
        println(pair)
    }

    @Test
    fun choicePair() {
        val choosePair = choosePair(0, 100)
        val rng = SimpleRNG(10)
        val pair = choosePair.sample.run(rng)
        println(pair.first)
    }

    @Test
    fun choicePair2() {
        val choosePair2 = choosePair2(0, 100)
        val rng = SimpleRNG(10)
        val pair = choosePair2.sample.run(rng)
        println(pair.first)
    }

    @Test
    fun sequence() {
        val ls = List.of(unit(1), unit(2), unit(3))
        val sequence = sequence(ls)
        val unit = unit(List.of(1, 2, 3))

        val rng = SimpleRNG(10)
        sequence.sample.run(rng) shouldBe unit.sample.run(rng)
    }

}