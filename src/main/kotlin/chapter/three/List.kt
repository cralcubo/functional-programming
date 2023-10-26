package chapter.three

import chapter.four.None
import chapter.four.Option
import chapter.four.Some
import chapter.three.List.Companion.foldRight
import java.lang.RuntimeException

/**
 * This a simple Linked List data structure
 * This linked list is an Algebraic Data Structure
 */
sealed class List<out A> {
    companion object {
        fun <A> of(vararg aa: A): List<A> {
            return when {
                aa.isEmpty() -> Nil
                else -> Cons(aa.first(), of(*aa.sliceArray(1 until aa.size)))
            }
        }

        fun <A> gen(size: Int, z: A,  f: (A) -> A) : List<A> {
            tailrec fun acc(n: Int, z: A, ls: List<A>) : List<A> {
                return if(n > 0) {
                    acc(n-1, f(z), Cons(z, ls))
                } else ls
            }

            return acc(size, z,  Nil).revert()
        }

        fun <A> concat(xxs: List<List<A>>): List<A> =
            foldRight(
                xxs,
                empty()
            ) { xs1, xs2 -> append(xs1, xs2) }

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

        fun <A> appendL(a1: List<A>, a2: List<A>): List<A> = foldLeft(a1, a2) { tl, a -> Cons(a, tl) }
        fun <A> appendR(a1: List<A>, a2: List<A>): List<A> = foldRight(a1,a2) { h, nt -> Cons(h, nt) }


        fun <A> init(l: List<A>): List<A> =
            when (l) {
                is Cons ->
                    if (l.tail == Nil) Nil
                    else Cons(l.head, init(l.tail))
                is Nil ->
                    throw IllegalStateException("Cannot init Nil list")
            }

        tailrec fun <A, B> foldLeft(xs: List<A>, z: B, f: (B, A) -> B): B {
            return when(xs) {
                is Nil -> z
                is Cons -> foldLeft(xs.tail, f(z, xs.head), f)
            }
        }

        fun sum3(ints: List<Int>): Int = foldLeft(ints, 0) { b, a -> b + a}
        fun product3(dbs: List<Double>): Double = foldLeft(dbs, 1.0) { b, a -> b * a}

        fun <A> revert(ls: List<A>) : List<A> = foldLeft(ls, empty()) { tail, head -> Cons(head, tail)}

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

        fun <A, B> foldLeftR(xs: List<A>, z: B, f: (B, A) -> B): B =
            foldRight(xs, { b: B -> b })
                { a, g -> { b -> g(f(b, a)) } }(z)

        fun <A, B> foldRightL(xs: List<A>, z: B, f: (A, B) -> B): B =
            foldLeft(xs,
                { b: B -> b },
                { g, a ->
                    { b ->
                        g(f(a, b))
                    }
                })(z)

        fun <A> filter(xs: List<A>, f: (A) -> Boolean): List<A> =
            foldRight(xs, empty()) { h, nl -> if (f(h)) Cons(h, nl) else nl }

        fun <A, B> map(xs: List<A>, f: (A) -> B): List<B> =
            foldRight(xs, empty()) { h, nl -> Cons(f(h), nl) }

        fun <A, B> flatMap(xa: List<A>, f: (A) -> List<B>): List<B> =
            concat( map(xa, f) )

        fun intAdd(xa: List<Int>, xb: List<Int>) : List<Int> = zipWith(xa, xb) { a, b -> a + b}


        fun <A> zipWith(xa: List<A>, xb: List<A>, f: (A, A) -> A) : List<A> =
            when(xa) {
                is Nil -> Nil
                is Cons -> when(xb) {
                    is Nil -> Nil
                    is Cons -> Cons(f(xa.head,xb.head), zipWith(xa.tail, xb.tail, f))
                }
            }

    }

    fun add1(ls: List<Int>) : List<Int> =
        foldRight(ls, empty()) {a , nl -> Cons(a+1, nl)}

}


object Nil : List<Nothing>()
data class Cons<out A>(val head: A, val tail: List<A>) : List<A>()

// Extensions
fun <A> List<A>.filter(f: (A) -> Boolean) = List.filter(this, f)
fun <A, B> List<A>.map(f: (A) -> B) = List.map(this, f)
fun <A, B> List<A>.flatMap(f: (A) -> List<B>) = List.flatMap(this, f)
fun <A> List<A>.zipWith(ls: List<A>, f: (A, A) -> A) = List.zipWith(this, ls, f)
fun <A, B> List<A>.foldRight(z: B, f: (A, B) -> B): B = when (this) {
    is Nil -> z
    is Cons -> f(this.head, foldRight(this.tail, z, f))
}

fun <A> List<A>.revert() = List.revert(this)

fun <A> List<A>.size() = this.foldRight(0){ _, x -> x + 1 }

fun <A> List<A>.first() =
    when (this) {
        is Nil -> None
        is Cons -> Some(this.head)
    }
fun <A> List<A>.last() : Option<A> {
    tailrec fun go(ls: List<A>, last: A): A {
        return when(ls) {
            is Nil -> last
            is Cons -> go(ls.tail, ls.head)
        }
    }
    return when(this) {
        is Nil -> None
        is Cons -> Some(go(this.tail, this.head))
    }
}


