package chapter.seven

import chapter.four.Option
import chapter.four.Some

fun <E> List<E>.splitAt(i: Int): Pair<List<E>, List<E>> {
    val (l, r) = this.chunked((this.size / 2) + (this.size % 2))
    return l to r
}

fun <E> List<E>.firstOption(): Option<E> =
    this.firstOrNull()?.let { Some(it) } ?: Option.empty()