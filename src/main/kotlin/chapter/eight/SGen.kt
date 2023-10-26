package chapter.eight

data class SGen<A>(val forSize: (Int) -> Gen<A>) {
    operator fun invoke(i: Int): Gen<A> =
        forSize(i)

    fun <A> unit(a: A): SGen<A> =
        SGen { Gen.unit(a) }

    fun <B> map(f: (A) -> B): SGen<B> =
        SGen { n -> this(n).map(f) }

    fun <B> flatMap(f: (A) -> Gen<B>): SGen<B> =
        SGen { n -> this(n).flatMap { a -> f(a) } }
}
