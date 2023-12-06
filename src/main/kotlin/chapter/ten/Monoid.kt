package chapter.ten

import chapter.four.None
import chapter.four.Option
import chapter.four.orElse
import chapter.three.List

interface Monoid<A> {
    fun combine(a1: A, a2:A) : A
    val nil: A
}

fun intAddition(): Monoid<Int> = object : Monoid<Int> {
    override fun combine(a1: Int, a2: Int) = a1 + a2
    override val nil = 0
}

fun intMultiplication(): Monoid<Int> = object : Monoid<Int> {
    override fun combine(a1: Int, a2: Int) = a1 * a2
    override val nil = 1
}

fun booleanOr(): Monoid<Boolean> = object : Monoid<Boolean> {
    override fun combine(a1: Boolean, a2: Boolean) = a1 || a2
    override val nil = false
}

fun booleanAnd(): Monoid<Boolean> = object : Monoid<Boolean> {
    override fun combine(a1: Boolean, a2: Boolean) = a1 && a2
    override val nil = true
}

fun <A> optionMonoid(): Monoid<Option<A>> = object : Monoid<Option<A>> {
    override fun combine(a1: Option<A>, a2: Option<A>): Option<A> =
        a1.orElse { a2 }
    override val nil: Option<A> = None
}

fun <A> dual(m: Monoid<A>): Monoid<A> = object : Monoid<A> {
    override fun combine(a1: A, a2: A): A = m.combine(a2, a1)
    override val nil: A = m.nil
}

fun <A> firstOptionMonoid() = optionMonoid<A>()
fun <A> lastOptionMonoid() = dual(firstOptionMonoid<A>())

fun <A> endoMonoid(): Monoid<(A) -> A> = object : Monoid<(A) -> A> {
    override fun combine(a1: (A) -> A, a2: (A) -> A): (A) -> A  = {a -> a2(a1(a))}
    override val nil: (A) -> A = { a -> a }
}

fun <A, B> foldRight(la: List<A>, z: B, f: (A, B) -> B): B =
    List.foldMap(la, endoMonoid()) { a: A -> { b: B -> f(a, b) } }(z)

fun <A, B> foldLeft(la: List<A>, z: B, f: (B, A) -> B): B =
    List.foldMap(la, dual(endoMonoid())) { a: A -> { b: B -> f(b, a) } }(z)



