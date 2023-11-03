package chapter.eight

import arrow.core.getOrElse
import arrow.core.toOption
import chapter.six.RNG
import chapter.six.SimpleRNG
import kotlin.math.min

typealias SuccessCount = Int
typealias FailedCase = String

typealias TestCases = Int
typealias MaxSize = Int

//typealias Result = Option<Pair<FailedCase, SuccessCount>>
data class Prop(val check: (MaxSize, TestCases, RNG) -> Result) {

    companion object {
        fun run(
            p: Prop,
            maxSize: Int = 100,
            testCases: Int = 100,
            rng: RNG = SimpleRNG(System.currentTimeMillis())
        ): Unit =
            when (val result = p.check(maxSize, testCases, rng)) {
                is Falsified ->
                    println(
                        "Falsified after ${result.successes}" +
                                " passed tests: ${result.failure}"
                    )
                is Passed ->
                    println("OK, passed $testCases tests.")
            }

        fun <A> forAll(g: SGen<A>, f: (A) -> Boolean): Prop =
            forAll({ i -> g(i) }, f)

        fun <A> forAll(g: (Int) -> Gen<A>, f: (A) -> Boolean): Prop =
            Prop { max, n, rng ->

                val casePerSize: Int = (n + (max - 1)) / max

                val props = generateSequence(0) { it + 1 }
                    .take(min(n, max) + 1)
                    .map { i -> forAll(g(i), f) }

                val prop: Prop = props.map { p ->
                    Prop { max, _, rng ->
                        p.check(max, casePerSize, rng)
                    }
                }.reduce { p1, p2 -> p1.and(p2) }
                prop.check(max, n, rng)
            }

    }

    fun and(p: Prop): Prop =
        Prop { max, n, rng ->
            when (val prop = check(max, n, rng)) {
                is Passed -> p.check(max, n, rng)
                is Falsified -> prop
            }
        }


}

private fun <A> forAll(ga: Gen<A>, f: (A) -> Boolean): Prop =
    Prop { _, n: TestCases, rng: RNG ->
        randomSequence(ga, rng).mapIndexed { i, a ->
            try {
                if (f(a)) Passed
                else Falsified(a.toString(), i)
            } catch (e: Exception) {
                Falsified(buildMessage(a, e), i)
            }
        }.take(n)
            .find { it.isFalsified() }
            .toOption()
            .getOrElse { Passed }
    }


private fun <A> randomSequence(ga: Gen<A>, rng: RNG): Sequence<A> =
    sequence {
        val (a: A, rng2: RNG) = ga.sample.run(rng)
        yield(a)
        yieldAll(randomSequence(ga, rng2))
    }

private fun <A> buildMessage(a: A, e: Exception) =
    """
    |test case: $a
    |generated and exception: ${e.message}
    |stacktrace:
    |${e.stackTrace.joinToString("\n")}
""".trimMargin()


sealed class Result {
    abstract fun isFalsified(): Boolean
}

object Passed : Result() {
    override fun isFalsified(): Boolean = false
}

data class Falsified(
    val failure: FailedCase, // String
    val successes: SuccessCount // Int
) : Result() {
    override fun isFalsified(): Boolean = true
}





