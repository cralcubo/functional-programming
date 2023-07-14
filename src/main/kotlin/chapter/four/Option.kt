package chapter.four

import chapter.three.Cons
import chapter.three.List
import chapter.three.Nil
import chapter.three.foldRight
import kotlin.math.abs

sealed class Option<out A> {
    fun <A, B, C> map2(a: Option<A>, b: Option<B>, f: (A, B) -> C): Option<C> =
        a.flatMap { x -> b.map { y -> f(x,y) } }

    fun <A, B> traverse(xa: List<A>, f: (A) -> Option<B>): Option<List<B>> =
        when (xa) {
            is Nil -> Some(Nil)
            is Cons -> map2(f(xa.head), traverse(xa.tail, f)) { b, xb -> Cons(b, xb) }
        }
    fun <A> sequence(xs: List<Option<A>>): Option<List<A>> =
        traverse(xs) { it }

}
data class Some<out A>(val get: A) : Option<A>()
object None : Option<Nothing>()

// Extension Functions
fun <A, B> Option<A>.map(f: (A) -> B): Option<B> =
    when(val o = this) {
        is None -> None
        is Some -> Some(f(o.get))
    }

fun <A> Option<A>.getOrElse(default: () -> A): A =
    when(val o = this) {
        is None -> default()
        is Some -> o.get
    }

fun <A, B> Option<A>.flatMap(f: (A) -> Option<B>): Option<B> =
    this.map(f).getOrElse { None }

fun <A> Option<A>.orElse(ob: () -> Option<A>): Option<A> =
    this.map { Some(it) }.getOrElse { ob() }

fun <A> Option<A>.filter(f: (A) -> Boolean): Option<A> =
    this.flatMap {  if(f(it)) Some(it) else None }




fun <A, B> lift(f: (A) -> B): (Option<A>) -> Option<B> =
    { oa -> oa.map(f) }

val absO: (Option<Double>) -> Option<Double> =
    lift { abs(it) }

fun main() {
    absO(Some(5.0))
}

