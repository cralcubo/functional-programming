package chapter.seven

class Par2<A>(val get: () -> A)

fun <A> unit2(a: () -> A): Par2<A> = Par2(a)
fun <A> get(a: Par2<A>): A = a.get()


//------ The book Par ----//
class Par<A>(val get: A) {

    companion object {
        fun <A, B, C> map2(pa: Par<A>, pb: Par<B>, f: (A, B) -> C): Par<C> =
            Par(f(pa.get, pb.get))
    }
}

fun <A> unit(a: () -> A): Par<A> = Par(a())

fun <A> get(a: Par<A>): A = a.get