package chapter.seven

import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

typealias Par<A> = (ExecutorService) -> Future<A>

object Pars {
    fun <A> unit(a: A): Par<A> = { UnitFuture(a) }

    fun <A> lazyUnit(a: () -> A): Par<A> = fork { unit(a()) }


    fun <A, B, C> map2(a: Par<A>, b: Par<B>, f: (A, B) -> C): Par<C> =
        { executorService ->
            val af: Future<A> = a(executorService)
            val bf: Future<B> = b(executorService)
            UnitFuture(f(af.get(),bf.get()))
        }

    fun <A, B> map(pa: Par<A>, f: (A) -> B): Par<B> = map2(pa, unit(Unit)) { a, _ -> f(a) }

    fun <A> fork(a: () -> Par<A>): Par<A> =
        { executorService ->
            println("Fork running on thread ${Thread.currentThread().name}")
            executorService.submit(Callable { a()(executorService).get() })
        }

    fun <A, B> asyncF(f: (A) -> B): (A) -> Par<B> =
        { a -> lazyUnit { f(a) } }

    fun <A> sequence(ps: List<Par<A>>): Par<List<A>> =
        when {
            ps.isEmpty() -> unit(emptyList())
            ps.size == 1 -> map(ps.head) { listOf(it) }
            else -> {
                val (l, r) = ps.splitAt(ps.size / 2)
                map2(sequence(l), sequence(r)) { la, lb -> la + lb }
            }
        }

    fun <A, B> parMap(ps: List<A>, f: (A) -> B): Par<List<B>> =
        fork {
            println("Mapping on thread ${Thread.currentThread().name}")
            val fbs: List<Par<B>> = ps.asSequence().map { fork { unit(f(it)) } }.toList()
            sequence(fbs)
        }

    fun <A> parFilter(la: List<A>, f: (A) -> Boolean): Par<List<A>> =
        fork {
            println("Filtering on thread ${Thread.currentThread().name}")
            val fla = la.filter(f).map(asyncF { it })
            sequence(fla)
        }

    private fun <A, B> flatMap(pa: Par<A>, choices: (A) -> Par<B>): Par<B> =
        { es: ExecutorService ->
            choices(pa(es).get())(es)
        }

    fun <A> join(a: Par<Par<A>>): Par<A> = {
        es -> a(es).get()(es)
    }

    fun <A> choice(cond: Par<Boolean>, t: Par<A>, f: Par<A>): Par<A> =
        flatMap(cond) { if (it) t else f }


}