package chapter.six

import chapter.six.State.Companion.map2
import chapter.six.State.Companion.unit
import chapter.three.Cons
import chapter.three.List
import chapter.three.foldRight

data class State<S, out A>(val run: (S) -> Pair<A, S>) {
    companion object {
        fun <S, A> unit(a: A): State<S, A> = State { s -> a to s }

        fun <S, A, B, C> map2(sa: State<S, A>, sb: State<S, B>, f: (A, B) -> C): State<S, C> =
            sa.flatMap { a -> sb.map { b -> f(a, b) } }

        fun <S, A> sequence(fs: List<State<S, A>>): State<S, List<A>> =
            fs.foldRight(unit(List.empty())) { sa, slb ->
                map2(sa,slb) { h,t -> Cons(h,t) }
            }

    }
}

// Extension functions
fun <S, A, B> State<S, A>.flatMap(f: (A) -> State<S, B>): State<S, B> =
    State { s ->
        val (a, ns) = this.run(s)
        f(a).run(ns)
    }

fun <S, A, B> State<S, A>.map(f: (A) -> B): State<S, B> =
    this.flatMap { a -> unit(f(a)) }
