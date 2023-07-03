package chapter.three

import java.lang.RuntimeException

sealed class List<out A> {
    companion object {
        fun <A> of(vararg aa: A): List<A> {
            return when {
                aa.isEmpty() -> Nil
                else -> Cons(aa.first(), of(*aa.sliceArray(1 until aa.size)))
            }
        }

        fun <A> tail(ls: List<A>) : List<A> =
            when(ls) {
                is Nil -> throw RuntimeException()
                is Cons -> ls.tail
            }

        fun <A> setHead(ls: List<A>, h: A) =
            when(ls) {
                is Nil -> throw RuntimeException()
                is Cons -> Cons(h, ls.tail)
            }


    }
}

object Nil : List<Nothing>()

data class Cons<out A>(val head: A, val tail: List<A>) : List<A>()