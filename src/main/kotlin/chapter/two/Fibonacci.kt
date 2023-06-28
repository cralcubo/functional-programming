package chapter.two

fun main() {

    println(fib(8))

}

/**
 * To produce: 0, 1, 1, 2, 3, 5, 8, 13, 21
 */
fun fib(i: Int) : Int {
    fun go(j: Int) : Int {
        return when(j) {
            0 -> 0
            1 -> 1
            else -> go(j - 1) + go(j - 2)
        }
    }
    return go(i)
}

fun fibTail(i: Int) : Int {
    tailrec fun go(cnt: Int, curr: Int, next: Int) : Int =
        when(cnt) {
            0 -> curr
            else -> go(cnt - 1, next, curr + next)
        }

    return go(i, 0, 1)
}