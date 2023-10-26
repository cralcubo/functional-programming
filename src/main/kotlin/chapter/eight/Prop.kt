package chapter.eight

import arrow.core.getOrElse
import arrow.core.toOption
import chapter.six.RNG

typealias SuccessCount = Int
typealias FailedCase = String

typealias TestCases = Int

//typealias Result = Option<Pair<FailedCase, SuccessCount>>

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

data class Prop(val check: (TestCases, RNG) -> Result) { // (Int, RNG) -> Result

    fun and(p: Prop): Prop =
        Prop { n: TestCases, rng: RNG ->
            val check1 = p.check(n,rng)
            val check2 = this.check(n,rng)
            when {
                check1 is Falsified && check2 is Falsified -> Falsified(check1.failure + check2.failure, check1.successes + check2.successes)
                check1 is Falsified -> check1
                check2 is Falsified -> check2
                else -> Passed
            }
        }

    fun or(p: Prop): Prop =
        Prop { n: TestCases, rng: RNG ->
            val check1 = p.check(n,rng)
            val check2 = this.check(n,rng)
            when {
                check1 is Passed || check2 is Passed -> Passed
                else -> check1
            }
        }

    fun <A> forAll(ga: Gen<A>, f: (A) -> Boolean): Prop =
        Prop { n: TestCases, rng: RNG ->
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






