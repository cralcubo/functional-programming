package chapter.six

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class StateTest {

    @Test
    fun unit() {
        val unit = State.unit<RNG, Int>(1)
        val state = SimpleRNG(1)
        val pair = unit.run(state)

        pair.first shouldBe 1
        pair.second shouldBe state
    }

    @Test
    fun map() {
        val state = State(RNG::nextInt)
        val state2 = state.map { it * 10 }

        val run = state2.run(SimpleRNG(1))
        val op = SimpleRNG(1).nextInt()
        run.first shouldBe op.first * 10
        run.second shouldBe op.second
    }

    @Test
    fun flatMap() {
        val state = State(RNG::nextInt)
        val state2 = state.flatMap { State.unit(it*10) }

        val r = state2.run(SimpleRNG(1))
        r.first shouldBe SimpleRNG(1).nextInt().first * 10
        r.second shouldBe SimpleRNG(1).nextInt().second
    }
}