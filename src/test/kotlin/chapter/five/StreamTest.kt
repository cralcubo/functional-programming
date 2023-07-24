package chapter.five

import chapter.three.List
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class StreamTest {

    @Test
    fun drop() {
        val stream = Cons({ 1 }, { Cons({ 2 }, { Cons({ 3 }, { Empty }) }) })
        stream.drop(2).toList() shouldBe List.of(3)
    }

    @Test
    fun take() {
        val stream = Stream.of(1, 2, 3, 4)
        stream.take(2).toList() shouldBe List.of(1, 2)
    }

    @Test
    fun map() {
        val stream = Stream.of(1, 2, 3, 4)
        stream.map { it * 10 }.toList() shouldBe List.of(10, 20, 30, 40)
    }

    @Test
    fun filter() {
        val stream = Stream.of(1, 2, 3, 4)
        stream.filter { it % 2 == 0 }.toList() shouldBe List.of(2, 4)
    }

    @Test
    fun append() {
        val stream = Stream.of(1, 2, 3, 4)
        stream.append { Stream.of(5, 6) }.toList() shouldBe List.of(1, 2, 3, 4, 5, 6)
    }

    @Test
    fun flatMap() {
        val s = Stream.of(1, 2, 3)
        s.flatMap { a -> Stream.of(a, a) }.toList() shouldBe List.of(1, 1, 2, 2, 3, 3)
    }

    @Test
    fun from2() {
        chapter.five.from2(3).take(2).toList() shouldBe List.of(3,4)
    }

}