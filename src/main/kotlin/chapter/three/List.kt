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

        fun <A> empty(): List<A> = Nil

        fun <A> tail(ls: List<A>): List<A> = drop(ls, 1)

        fun <A> setHead(ls: List<A>, h: A) = when (ls) {
            is Nil -> throw RuntimeException()
            is Cons -> Cons(h, ls.tail)
        }

        tailrec fun <A> drop(l: List<A>, n: Int): List<A> =
            if(n == 0) l
            else when(l) {
                is Nil -> throw RuntimeException("You cannot drop more elements")
                is Cons -> drop(l.tail, n -1)
            }

        tailrec fun <A> dropWhile(l: List<A>, f: (A) -> Boolean): List<A> =
            when(l) {
                is Cons -> if(f(l.head)) dropWhile(l.tail, f) else l
                is Nil -> l
            }

        fun <A> append(a1: List<A>, a2: List<A>): List<A> =
            when (a1) {
                is Nil -> a2
                is Cons -> Cons(a1.head, append(a1.tail, a2))
            }

        fun <A> init(l: List<A>): List<A> =
            when (l) {
                is Cons ->
                    if (l.tail == Nil) Nil
                    else Cons(l.head, init(l.tail))
                is Nil ->
                    throw IllegalStateException("Cannot init Nil list")
            }

        fun <A, B> foldRight(xs: List<A>, z: B, f: (A, B) -> B): B =
            when (xs) {
                is Nil -> z
                is Cons -> f(xs.head, foldRight(xs.tail, z, f))
            }

        fun sum2(ints: List<Int>): Int =
            foldRight(ints, 0) { a, b -> a + b }

        fun product2(dbs: List<Double>): Double =
            foldRight(dbs, 1.0) { a, b -> a * b }

        fun <A> length(xs: List<A>): Int =
            foldRight(xs, 0) {_,b -> 1 + b}


    }

}


object Nil : List<Nothing>()
data class Cons<out A>(val head: A, val tail: List<A>) : List<A>()