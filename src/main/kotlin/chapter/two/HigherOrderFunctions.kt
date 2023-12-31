package chapter.two


val <T> List<T>.tail: List<T>
    get() = drop(1)

val <T> List<T>.head: T
    get() = first()

fun <A> isSorted(aa: List<A>, order: (A, A) -> Boolean): Boolean {
    tailrec fun loop(i: Int, eval: Boolean) : Boolean {
        return when {
            i + 1 >= aa.size -> eval
            else -> loop(i + 1, eval && order(aa[i], aa[i+1]))
        }
    }

    return loop(0, true)
}

fun <A> isSortedBook(aa: List<A>, order: (A, A) -> Boolean): Boolean {
    tailrec fun go(x: A, xs: List<A>): Boolean =
        if (xs.isEmpty()) true
        else if (!order(x, xs.head)) false
        else go(xs.head, xs.tail)

    return aa.isEmpty() || go(aa.head, aa.tail)
}

fun <A, B, C> partial1(a: A, f: (A, B) -> C): (B) -> C = { b -> f(a,b) }

fun <A, B, C> curry(f: (A, B) -> C): (A) -> (B) -> C = { a -> { b -> f(a, b) } }

fun <A, B, C> uncurry(f: (A) -> (B) -> C): (A, B) -> C = { a, b -> f(a)(b) }

fun <A, B, C> compose(f: (B) -> C, g: (A) -> B): (A) -> C = { a -> f(g(a)) }





