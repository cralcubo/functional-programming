package chapter.seven

import java.util.concurrent.Future
import java.util.concurrent.TimeUnit


data class TimedMap2Future<A, B, C>(
    val pa: Future<A>,
    val pb: Future<B>,
    val f: (A, B) -> C
) : Future<C> {
    override fun isDone(): Boolean = TODO("Unused")
    override fun get(): C = TODO("Unused")
    override fun cancel(b: Boolean): Boolean = TODO("Unused")
    override fun isCancelled(): Boolean = TODO("Unused")

    override fun get(timeout: Long, unit: TimeUnit): C {
        val timeoutMillis = TimeUnit.MILLISECONDS.convert(timeout, unit)
        val start = System.currentTimeMillis()
        val a = pa.get(timeout, unit)
        val duration = System.currentTimeMillis() - start

        val reminder = timeoutMillis - duration
        val b = pb.get(reminder, TimeUnit.MILLISECONDS)

        return f(a, b)
    }

}

data class UnitFuture<A>(val a: A) : Future<A> {

    override fun get(): A = a

    override fun get(timeout: Long, timeUnit: TimeUnit): A = a

    override fun cancel(evenIfRunning: Boolean): Boolean = false

    override fun isDone(): Boolean = true

    override fun isCancelled(): Boolean = false
}