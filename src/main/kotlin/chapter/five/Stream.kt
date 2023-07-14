package chapter.five

import chapter.four.None
import chapter.four.Option
import chapter.four.Some

sealed class Stream<out A> {
    companion object {
        fun <A> cons(hd: () -> A, tl: () -> Stream<A>): Stream<A> {
            val head: A by lazy(hd)
            val tail: Stream<A> by lazy(tl)
            return Cons({ head }, { tail })
        }

        fun <A> empty(): Stream<A> = Empty
        fun <A> of(vararg xs: A): Stream<A> =
            when {
                xs.isEmpty() -> empty()
                else -> cons(
                    { xs[0] },
                    { of(*xs.sliceArray(1 until xs.size)) }
                )
            }
    }
}

data class Cons<out A>(val head: () -> A, val tail: () -> Stream<A>) : Stream<A>()

object Empty : Stream<Nothing>()

// Extension functions
fun <A> Stream<A>.headOption(): Option<A> =
    when (this) {
        is Empty -> None
        is Cons -> Some(head())
    }