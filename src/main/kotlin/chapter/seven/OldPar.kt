package chapter.seven

import chapter.seven.OldPar.Companion.map2
import chapter.seven.OldPar.Companion.unit


//------ The book Par ----//
class OldPar<A>(val get: A) {

    companion object {
        fun <A, B, C> map2(pa: OldPar<A>, pb: OldPar<B>, f: (A, B) -> C): OldPar<C> =
            OldPar(f(pa.get, pb.get))

        fun <A> unit(a: A): OldPar<A> = OldPar(a)

        fun <A> lazyUnit(a: () -> A): OldPar<A> = fork { unit(a()) }

        fun <A> run(a: OldPar<A>): A = a.get

        fun <A> fork(a: () -> OldPar<A>): OldPar<A> = a()
    }
}

fun <A,B> OldPar<A>.map(f: (A) -> B): OldPar<B> = map2(this, unit(Unit)) { a, _ -> f(a) }


