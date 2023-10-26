package chapter.eight

import chapter.six.*
import chapter.three.Cons
import chapter.three.List
import chapter.three.foldRight
import kotlin.math.absoluteValue

data class Gen<A>(val sample: State<RNG, A>) {
    fun unsized(): SGen<A> = SGen { this }
    fun listOf(): SGen<List<A>> = TODO()

    companion object {

        fun <A> unit(a: A): Gen<A> = Gen(State.unit(a))

        fun <A> union(ga: Gen<A>, gb: Gen<A>): Gen<A> =
            boolean().flatMap { if(it) ga else gb }

        fun <A> weighted(
            pga: Pair<Gen<A>, Double>,
            pgb: Pair<Gen<A>, Double>
        ): Gen<A> {
            val (ga, wa) = pga
            val (gb, wb) = pgb
            val prob = wa.absoluteValue / (wa.absoluteValue + wb.absoluteValue)
            return Gen(State { rng: RNG -> double(rng) })
                .flatMap { d -> if (d < prob) ga else gb }
        }


        fun <A,B,C> map2(ga: Gen<A>, gb: Gen<B>, f: (A,B) -> C) : Gen<C> =
            ga.flatMap { a -> gb.map { b -> f(a,b) } }

        fun <A> sequence(lg: List<Gen<A>>) : Gen<List<A>> =
            lg.foldRight(unit(List.empty())) { ga, gla ->
                map2(ga, gla) { a, la -> Cons(a, la) }
            }

        fun <A> listOfN(n: Int, ga: Gen<A>): Gen<List<A>> {
            val sequence = State.sequence(List.gen(n, ga.sample) { it })
            return Gen(sequence)
        }

        fun <A> listOfN2(gn: Gen<Int>, ga: Gen<A>) =
            gn.flatMap { n -> sequence(List.gen(n, ga) { it }) }
    }
}

// Extensions
fun <A,B> Gen<A>.map(f: (A) -> B) : Gen<B> =
    this.flatMap { a -> Gen.unit(f(a)) }

fun <A,B> Gen<A>.flatMap(f: (A) -> Gen<B>): Gen<B> =
    Gen(State { rng: RNG ->
        val (a, ns) = this.sample.run(rng)
        f(a).sample.run(ns)
    })


// Others
fun choose(start: Int, stopExclusive: Int): Gen<Int> =
    Gen(State { rng: RNG -> nonNegativeInt(rng) }
        .map { start + (it % (stopExclusive - start)) })

fun choosePair(start: Int, stopExclusive: Int): Gen<Pair<Int, Int>> =
    Gen(State { rng: RNG ->
        val pair1 = choose(start, stopExclusive).sample.run(rng)
        val pair2 = choose(start, stopExclusive).sample.run(pair1.second)
        Pair(Pair(pair1.first, pair2.first), pair2.second)
    })

fun choosePair2(start: Int, stopExclusive: Int): Gen<Pair<Int, Int>> =
    Gen.map2(choose(start, stopExclusive), choose(start, stopExclusive)) { a, b -> Pair(a, b) }

fun boolean(): Gen<Boolean> =
    Gen(State{ rng: RNG ->
        val r = nonNegativeInt(rng)
        Pair(r.first % 2 == 0, r.second)
    })
