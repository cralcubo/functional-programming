package chapter.five

import chapter.five.Stream.Companion.cons
import chapter.five.Stream.Companion.empty
import chapter.five.Stream.Companion.unfold
import chapter.four.*
import chapter.three.Nil
import chapter.three.List
import chapter.three.Cons as ConsL

sealed class Stream<out A> {
    companion object {

        fun <A, S> unfold(z: S, f: (S) -> Option<Pair<A, S>>): Stream<A> =
            f(z).map { pair -> cons({ pair.first }, { unfold(pair.second, f) }) }
                .getOrElse { empty() }

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

fun <A> Stream<A>.headOption2(): Option<A> =
    this.foldRight({ Option.empty() }, { a, _ -> Some(a) })


fun <A> Stream<A>.toList(): List<A> {
    tailrec fun go(xs: Stream<A>, acc: List<A>): List<A> =
        when (xs) {
            is Empty -> acc
            is Cons -> go(xs.tail(), ConsL(xs.head(), acc))
        }
    return List.revert(go(this, Nil))
}

fun <A> Stream<A>.take(n: Int): Stream<A> {
    tailrec fun go(xs: Stream<A>, counter: Int, acc: Stream<A>): Stream<A> {
        return when (counter) {
            n -> acc
            else -> when (xs) {
                is Empty -> throw IllegalArgumentException("You cannot take  more than what is available")
                is Cons -> go(xs.tail(), counter + 1, Cons(xs.head) { acc })
            }
        }
    }

    return go(this, 0, Empty)
}

fun <A> Stream<A>.drop(n: Int): Stream<A> {
    tailrec fun go(xs: Stream<A>, counter: Int): Stream<A> {
        return when (counter) {
            n -> xs
            else -> when (xs) {
                is Empty -> xs
                is Cons -> go(xs.tail(), counter + 1)
            }
        }
    }

    return go(this, 0)
}

fun <A> Stream<A>.takeWhile(p: (A) -> Boolean): Stream<A> =
    when (this) {
        is Empty -> empty()
        is Cons ->
            if (p(this.head()))
                cons(this.head) { this.tail().takeWhile(p) }
            else empty()
    }

fun <A> Stream<A>.takeWhile2(p: (A) -> Boolean): Stream<A> =
    this.foldRight({ empty() }) { a, b -> if (p(a)) cons({ a }, b) else b() }


fun <A, B> Stream<A>.foldRight(z: () -> B, f: (A, () -> B) -> B): B =
    when (this) {
        is Cons -> f(this.head()) {
            tail().foldRight(z, f)
        }

        is Empty -> z()
    }

fun <A> Stream<A>.exists(p: (A) -> Boolean): Boolean =
    this.foldRight({ false }, { a, b -> p(a) || b() })

fun <A> Stream<A>.forAll(p: (A) -> Boolean): Boolean =
    this.foldRight({ true }, { a, b -> p(a) && b() })

fun <A, B> Stream<A>.map(f: (A) -> B): Stream<B> =
    this.foldRight({ empty() }, { a, ta -> cons({ f(a) }, ta) })

fun <A> Stream<A>.filter(p: (A) -> Boolean): Stream<A> =
    this.foldRight({ empty() }, { a, lt -> if (p(a)) cons({ a }, lt) else lt() })

fun <A> Stream<A>.append(ns: () -> Stream<A>): Stream<A> =
    foldRight(ns) { a, lt -> cons({ a }, lt) }

fun <A, B> Stream<A>.flatMap(f: (A) -> Stream<B>): Stream<B> =
    foldRight({ empty() }) { a, ltb -> f(a).append(ltb) }

fun <A> Stream<A>.find(p: (A) -> Boolean): Option<A> =
    filter(p).headOption()


// Test functions
fun from(n: Int): Stream<Int> = cons({ n }, { from(n + 1) })
fun from2(n: Int): Stream<Int> = unfold(n) { nr -> Some(Pair(nr, nr + 1) )  }

// 0, 1, 1, 2, 3, 5, 8
fun fibs(): Stream<Int> {
    fun go(i: Int, j: Int): Stream<Int> =
        cons({ i }, { go(j, i + j) })
    return go(0, 1)
}


