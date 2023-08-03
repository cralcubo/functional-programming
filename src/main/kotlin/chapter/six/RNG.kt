package chapter.six

import chapter.five.Stream
import chapter.five.take
import chapter.five.toList
import chapter.four.Some
import chapter.four.getOrElse
import chapter.three.*
import chapter.three.List


interface RNG {
    fun nextInt(): Pair<Int, RNG>
}

data class SimpleRNG(val seed: Long) : RNG {
    override fun nextInt(): Pair<Int, RNG> {
        val newSeed =
            (seed * 0x5DEECE66DL + 0xBL) and
                    0xFFFFFFFFFFFFL
        val nextRNG = SimpleRNG(newSeed)
        val n = (newSeed ushr 16).toInt()
        return n to nextRNG
    }
}

typealias Rand<A> = State<RNG, A>

val intR: Rand<Int> = State { rng -> rng.nextInt() }
val nonNegativeInt: Rand<Int> = State {rng -> nonNegativeInt(rng) }
val doubleR: Rand<Double> = map(nonNegativeInt) { it / (Int.MAX_VALUE.toDouble() + 1) }

fun <A> unit(a: A): Rand<A> = State.unit(a)


fun <A, B> flatMap(f: Rand<A>, g: (A) -> Rand<B>): Rand<B> = f.flatMap{a -> g(a)}
fun <A, B> mapFM(s: Rand<A>, f: (A) -> B): Rand<B> =
    flatMap(s) { unit(f(it)) }

fun <A, B> map(s: Rand<A>, f: (A) -> B): Rand<B> = s.map(f)

fun <A, B, C> map2FM(
    ra: Rand<A>,
    rb: Rand<B>,
    f: (A, B) -> C
): Rand<C> =
    flatMap(ra) {a ->
        mapFM(rb) {b ->
            f(a,b)
        }
    }

fun <A, B, C> map2(
    ra: Rand<A>,
    rb: Rand<B>,
    f: (A, B) -> C
): Rand<C> = State.map2(ra, rb, f)


fun nonNegativeIntLessThan(n: Int): Rand<Int> =
    State { rng ->
        val (i, rng2) = nonNegativeInt(rng)
        val mod = i % n
        if (i + (n - 1) - mod >= 0)
            mod to rng2
        else nonNegativeIntLessThan(n).run(rng2)
    }

fun nonNegativeIntLessThan2(n: Int): Rand<Int> =
    flatMap(nonNegativeInt) { i ->
        val mod = i % n
        if (i + (n - 1) - mod >= 0) unit(mod)
        else nonNegativeIntLessThan2(n)
    }

fun <A> sequence(fs: List<Rand<A>>): Rand<List<A>> =
    fs.foldRight(unit(List.empty())) { f, acc -> map2(f, acc) { h, t -> Cons(h, t) } }

fun <A, B> both(ra: Rand<A>, rb: Rand<B>): Rand<Pair<A, B>> =
    map2(ra, rb) { a, b -> a to b }



fun nonNegativeInt(rng: RNG): Pair<Int, RNG> {
    val (i1, rng2) = rng.nextInt()
    return (if (i1 < 0) -(i1 + 1) else i1) to rng2
}

fun nonNegativeEven(): Rand<Int> =
    map(nonNegativeInt) { it - (it % 2) }

fun double(rng: RNG): Pair<Double, RNG> {
    val (i, rng2) = nonNegativeInt(rng)
    return (i / (Int.MAX_VALUE.toDouble() + 1)) to rng2
}

fun intDouble(rng: RNG): Pair<Pair<Int, Double>, RNG> {
    val nextInt = rng.nextInt()
    val nextDbl = double(nextInt.second)
    return Pair(nextInt.first, nextDbl.first) to nextDbl.second
}

fun doubleInt(rng: RNG): Pair<Pair<Double, Int>, RNG> {
    val nextDbl = double(rng)
    val nextInt = nextDbl.second.nextInt()
    return Pair(nextDbl.first, nextInt.first) to nextInt.second
}

fun double3(rng: RNG): Pair<Triple<Double, Double, Double>, RNG> {
    val d1 = double(rng)
    val d2 = double(d1.second)
    val d3 = double(d2.second)
    return Triple(d1.first, d2.first, d3.first) to d3.second
}

fun ints(count: Int, rng: RNG): Pair<List<Int>, RNG> {
    val listRNG = Stream.unfold(rng.nextInt()) { Some(it to it.second.nextInt()) }
        .take(count)
        .toList()

    return listRNG.map { it.first } to listRNG.map { it.second }.last().getOrElse { rng }
}



