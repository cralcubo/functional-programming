package chapter.seven

import chapter.four.getOrElse
import chapter.seven.Par.Companion.map2


fun sum(ints: List<Int>): Par<Int> =
    if (ints.size <= 1)
        unit { ints.firstOption().getOrElse { 0 } }
    else {
        val (l, r) = ints.splitAt(ints.size / 2)
        map2(sum(l), sum(r)) { lx: Int, rx: Int ->
            println("Evaluating $lx and $rx")
            lx + rx
        }
    }
